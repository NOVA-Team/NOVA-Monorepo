package nova.core.render.model;

import nova.core.util.exception.NovaException;
import nova.core.util.transform.Quaternion;
import nova.core.util.transform.Vector2d;
import nova.core.util.transform.Vector3d;
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
import java.util.stream.IntStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

/**
 * A Techne model importer.
 * You must load your .tcn file and then bind the Techne texture yourself.
 * @author Calclavia
 */
public class TechneModel implements ModelProvider {

	//Identifiers for cubes
	public static final List<String> cubeIDs = Arrays.asList("d9e621f7-957f-4b77-b1ae-20dcd0da7751", "de81aa14-bd60-4228-8d8d-5238bcd3caaa");
	//The name of the file
	public final String name;
	//A map of all models generated with their names
	private final Model model = new Model();

	public TechneModel(String name) {
		this.name = name;
	}

	private void loadTechneModel(InputStream stream) throws NovaException {
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
				throw new NovaException("Model " + name + " contains no model.xml file");
			}

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(new ByteArrayInputStream(modelXml));

			NodeList nodeListTechne = document.getElementsByTagName("Techne");
			if (nodeListTechne.getLength() < 1) {
				throw new NovaException("Model " + name + " contains no Techne tag");
			}

			NodeList nodeListModel = document.getElementsByTagName("Model");
			if (nodeListModel.getLength() < 1) {
				throw new NovaException("Model " + name + " contains no Model tag");
			}

			NamedNodeMap modelAttributes = nodeListModel.item(0).getAttributes();
			if (modelAttributes == null) {
				throw new NovaException("Model " + name + " contains a Model tag with no attributes");
			}

			NodeList textureSize = document.getElementsByTagName("TextureSize");

			NodeList shapes = document.getElementsByTagName("Shape");

			IntStream.range(0, shapes.getLength()).forEach(i -> {

				Node shape = shapes.item(i);
				NamedNodeMap shapeAttributes = shape.getAttributes();
				if (shapeAttributes == null) {
					throw new NovaException("Shape #" + (i + 1) + " in " + name + " has no attributes");
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
				if (!(shapeType != null && !cubeIDs.contains(shapeType))) {

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

							if (shapeChildName.equals("IsMirrored")) {
								mirrored = !shapeChildValue.equals("False");
							} else if (shapeChildName.equals("Offset")) {
								offset = shapeChildValue.split(",");
							} else if (shapeChildName.equals("Position")) {
								position = shapeChildValue.split(",");
							} else if (shapeChildName.equals("Rotation")) {
								rotation = shapeChildValue.split(",");
							} else if (shapeChildName.equals("Size")) {
								size = shapeChildValue.split(",");
							} else if (shapeChildName.equals("TextureOffset")) {
								textureOffset = shapeChildValue.split(",");
							}
						}
					}

					// Generate new models
					final String modelName = shapeName;
					Model modelPart = new Model(modelName);
					modelPart.drawCube();
					modelPart.translation = new Vector3d(Double.parseDouble(position[0]), Double.parseDouble(position[1]) - 16, Double.parseDouble(position[2]));
					modelPart.offset = new Vector3d(Double.parseDouble(offset[0]), Double.parseDouble(offset[1]), Double.parseDouble(offset[2]));
					modelPart.rotation = Quaternion.fromEuler(Math.toRadians(Double.parseDouble(rotation[0])), Math.toRadians(Double.parseDouble(rotation[1])), Math.toRadians(Double.parseDouble(rotation[2])));
					modelPart.scale = new Vector3d(Integer.parseInt(size[0]), Integer.parseInt(size[1]), Integer.parseInt(size[2]));
					modelPart.textureOffset = new Vector2d(Integer.parseInt(textureOffset[0]), Integer.parseInt(textureOffset[1]));

					model.children.add(modelPart);
					if (model.children.stream().anyMatch(m -> m.name.equals(modelName))) {
						throw new NovaException("Model contained duplicate part name: '" + shapeName + "' node #" + i);
					}
				} else {
					System.out.println("Model shape [" + shapeName + "] in " + this.name + " is not a cube, ignoring");
				}
			});
		} catch (ZipException e) {
			throw new NovaException("Model " + name + " is not a valid zip file");
		} catch (IOException e) {
			throw new NovaException("Model " + name + " could not be read", e);
		} catch (SAXException e) {
			throw new NovaException("Model " + name + " contains invalid XML", e);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Model getModel() {
		return model;
	}

	@Override
	public String getType() {
		return "tcn";
	}
}