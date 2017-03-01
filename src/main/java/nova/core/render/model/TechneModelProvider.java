/*
 * Copyright (c) 2015 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */package nova.core.render.model;

import nova.core.render.RenderException;
import nova.core.render.pipeline.BlockRenderPipeline;
import nova.core.render.pipeline.CubeTextureCoordinates;
import nova.core.util.math.MatrixStack;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

/**
 * A Techne model importer.
 * You must load your .tcn file and then bind the Techne texture yourself.
 * @author Calclavia
 */
public class TechneModelProvider extends ModelProvider {

	//Identifiers for cubes
	public static final List<String> cubeIDs = Arrays.asList("d9e621f7-957f-4b77-b1ae-20dcd0da7751", "de81aa14-bd60-4228-8d8d-5238bcd3caaa");

	//A map of all models generated with their names
	private final MeshModel model = new MeshModel();

	/**
	 * Creates new ModelProvider
	 * @param domain dolain of the assets.
	 * @param name name of the model.
	 */
	public TechneModelProvider(String domain, String name) {
		super(domain, name);
	}

	@Override
	public void load(InputStream stream) {
		try {
			Map<String, byte[]> zipContents = new HashMap<>();
			ZipInputStream zipInput = new ZipInputStream(stream);
			ZipEntry entry;
			while ((entry = zipInput.getNextEntry()) != null) {
				byte[] data = new byte[(int) entry.getSize()];
				// For some reason, using read(byte[]) makes reading stall upon reaching a 0x1E byte
				int i = 0;
				while (zipInput.available() > 0 && i < data.length) {
					data[i++] = (byte) zipInput.read();
				}
				zipContents.put(entry.getName(), data);
			}

			byte[] modelXml = zipContents.get("model.xml");
			if (modelXml == null) {
				throw new RenderException("Model " + this.name + " contains no model.xml file");
			}

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(new ByteArrayInputStream(modelXml));

			NodeList nodeListTechne = document.getElementsByTagName("Techne");
			if (nodeListTechne.getLength() < 1) {
				throw new RenderException("Model " + this.name + " contains no Techne tag");
			}

			NodeList nodeListModel = document.getElementsByTagName("Model");
			if (nodeListModel.getLength() < 1) {
				throw new RenderException("Model " + this.name + " contains no Model tag");
			}

			NamedNodeMap modelAttributes = nodeListModel.item(0).getAttributes();
			if (modelAttributes == null) {
				throw new RenderException("Model " + this.name + " contains a Model tag with no attributes");
			}

			NodeList textureSize = document.getElementsByTagName("TextureSize");
			if (textureSize.getLength() == 0)
				throw new RenderException("Model has no texture size");

			String[] textureDimensions = textureSize.item(0).getTextContent().split(",");
			double textureWidth = Integer.parseInt(textureDimensions[0]);
			double textureHeight = Integer.parseInt(textureDimensions[1]);

			NodeList shapes = document.getElementsByTagName("Shape");

			for (int i = 0; i < shapes.getLength(); i++) {
				Node shape = shapes.item(i);
				NamedNodeMap shapeAttributes = shape.getAttributes();
				if (shapeAttributes == null) {
					throw new RenderException("Shape #" + (i + 1) + " in " + this.name + " has no attributes");
				}

				Node name = shapeAttributes.getNamedItem("name");
				String shapeName = null;
				if (name != null) {
					shapeName = name.getNodeValue();
				}
				if (shapeName == null) {
					shapeName = "Shape #" + (i + 1);
				}

				String shapeType = null;
				Node type = shapeAttributes.getNamedItem("type");
				if (type != null) {
					shapeType = type.getNodeValue();
				}

				if (shapeType != null && !cubeIDs.contains(shapeType)) {
					System.out.println("Model shape [" + shapeName + "] in " + this.name + " is not a cube, ignoring");
					continue;
				}

				boolean mirrored = false;
				String[] offset = new String[3];
				String[] position = new String[3];
				String[] rotation = new String[3];
				String[] size = new String[3];
				String[] textureOffset = new String[2];

				NodeList shapeChildren = shape.getChildNodes();
				for (int j = 0; j < shapeChildren.getLength(); j++) {
					Node shapeChild = shapeChildren.item(j);

					String shapeChildName = shapeChild.getNodeName();
					String shapeChildValue = shapeChild.getTextContent();
					if (shapeChildValue != null) {
						shapeChildValue = shapeChildValue.trim();

						switch (shapeChildName) {
							case "IsMirrored":
								mirrored = !shapeChildValue.equals("False");
								break;
							case "Offset":
								offset = shapeChildValue.split(",");
								break;
							case "Position":
								position = shapeChildValue.split(",");
								break;
							case "Rotation":
								rotation = shapeChildValue.split(",");
								break;
							case "Size":
								size = shapeChildValue.split(",");
								break;
							case "TextureOffset":
								textureOffset = shapeChildValue.split(",");
								break;
						}
					}
				}

				/*
				  	Generate new models
				  	Models in Techne are based on cubes.
				  	Each cube is, by default, skewed to the side. They are not centered.

				  	Everything is scaled by a factor of 16.
				  	The y coordinate is inversed, y = 24 is the surface
				  	The z coordinate is inverted, too.
				 */
				double positionX = Double.parseDouble(position[0]) / 16d;
				double positionY = (16 - Double.parseDouble(position[1])) / 16d;
				double positionZ = -Double.parseDouble(position[2]) / 16d;

				double sizeX = Double.parseDouble(size[0]) / 16d;
				double sizeY = Double.parseDouble(size[1]) / 16d;
				double sizeZ = Double.parseDouble(size[2]) / 16d;

				double offsetX = Double.parseDouble(offset[0]) / 16d;
				double offsetY = -Double.parseDouble(offset[1]) / 16d;
				double offsetZ = -Double.parseDouble(offset[2]) / 16d;

				double angleX = -Math.toRadians(Double.parseDouble(rotation[0]));
				double angleY = Math.toRadians(Double.parseDouble(rotation[1]));
				double angleZ = Math.toRadians(Double.parseDouble(rotation[2]));

				double textureOffsetU = Double.parseDouble(textureOffset[0]);
				double textureOffsetV = Double.parseDouble(textureOffset[1]);

				CubeTextureCoordinates textureCoordinates = new TechneCubeTextureCoordinates(
					textureWidth, textureHeight,
					textureOffsetU, textureOffsetV,
					sizeX, sizeY, sizeZ);

				final String modelName = shapeName;
				MeshModel modelPart = new MeshModel(modelName);
				BlockRenderPipeline.drawCube(
					modelPart,
					offsetX,
					offsetY - sizeY,
					offsetZ - sizeZ,
					offsetX + sizeX,
					offsetY,
					offsetZ,
					textureCoordinates);

				MatrixStack ms = new MatrixStack();
				ms.translate(positionX, positionY, positionZ);
				ms.rotate(Vector3D.PLUS_J, angleY);
				ms.rotate(Vector3D.PLUS_I, angleX);
				ms.rotate(Vector3D.PLUS_K, angleZ);
				modelPart.matrix = ms;
				modelPart.textureOffset = new Vector2D(Integer.parseInt(textureOffset[0]), Integer.parseInt(textureOffset[1]));

				if (model.children.stream().anyMatch(m -> m.name.equals(modelName))) {
					throw new RenderException("Model contained duplicate part name: '" + shapeName + "' node #" + i);
				}

				model.children.add(modelPart);
			}
		} catch (ZipException e) {
			throw new RenderException("Model " + this.name + " is not a valid zip file");
		} catch (IOException e) {
			throw new RenderException("Model " + this.name + " could not be read", e);
		} catch (SAXException e) {
			throw new RenderException("Model " + this.name + " contains invalid XML", e);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public MeshModel getModel() {
		return model.clone();
	}

	@Override
	public String getType() {
		return "tcn";
	}

	private static class TechneCubeTextureCoordinates implements CubeTextureCoordinates {
		private final double textureWidth;
		private final double textureHeight;
		private final double offsetU;
		private final double offsetV;
		private final double sizeX;
		private final double sizeY;
		private final double sizeZ;

		private TechneCubeTextureCoordinates(
			double textureWidth, double textureHeight,
			double offsetU, double offsetV,
			double sizeX, double sizeY, double sizeZ) {
			this.textureWidth = textureWidth;
			this.textureHeight = textureHeight;
			this.offsetU = offsetU;
			this.offsetV = offsetV;
			this.sizeX = sizeX * 16;
			this.sizeY = sizeY * 16;
			this.sizeZ = sizeZ * 16;
		}

		private double translateU(double pixelsU) {
			return (offsetU + pixelsU) / textureWidth;
		}

		private double translateV(double pixelsV) {
			return (offsetV + pixelsV) / textureHeight;
		}

		@Override
		public double getTopMinU() {
			return translateU(sizeZ + sizeX);
		}

		@Override
		public double getTopMinV() {
			return translateV(sizeZ);
		}

		@Override
		public double getTopMaxU() {
			return translateU(sizeZ);
		}

		@Override
		public double getTopMaxV() {
			return translateV(0);
		}

		@Override
		public double getBottomMinU() {
			return translateU(sizeZ + 2 * sizeX);
		}

		@Override
		public double getBottomMinV() {
			return translateV(0);
		}

		@Override
		public double getBottomMaxU() {
			return translateU(sizeZ + sizeX);
		}

		@Override
		public double getBottomMaxV() {
			return translateV(sizeZ);
		}

		@Override
		public double getWestMinU() {
			return translateU(0);
		}

		@Override
		public double getWestMinV() {
			return translateV(sizeZ);
		}

		@Override
		public double getWestMaxU() {
			return translateU(sizeZ);
		}

		@Override
		public double getWestMaxV() {
			return translateV(sizeZ + sizeY);
		}

		@Override
		public double getEastMinU() {
			return translateU(sizeX + sizeZ * 2);
		}

		@Override
		public double getEastMinV() {
			return translateV(sizeZ);
		}

		@Override
		public double getEastMaxU() {
			return translateU(sizeX + sizeZ);
		}

		@Override
		public double getEastMaxV() {
			return translateV(sizeZ + sizeY);
		}

		@Override
		public double getNorthMinU() {
			return translateU(sizeX + 2 * sizeZ);
		}

		@Override
		public double getNorthMinV() {
			return translateV(sizeZ);
		}

		@Override
		public double getNorthMaxU() {
			return translateU(2 * sizeX + 2 * sizeZ);
		}

		@Override
		public double getNorthMaxV() {
			return translateV(sizeZ + sizeY);
		}

		@Override
		public double getSouthMinU() {
			return translateU(sizeZ);
		}

		@Override
		public double getSouthMinV() {
			return translateV(sizeZ);
		}

		@Override
		public double getSouthMaxU() {
			return translateU(sizeX + sizeZ);
		}

		@Override
		public double getSouthMaxV() {
			return translateV(sizeZ + sizeY);
		}
	}
}
