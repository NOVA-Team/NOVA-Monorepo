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
 */

package nova.core.render.model;

import nova.core.render.RenderException;
import nova.core.util.math.Vector3DUtil;
import nova.internal.core.Game;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A OBJ model importer.
 * You must load your .obj file and then bind the OBJ texture yourself.
 *
 * @author Thog
 */
public class WavefrontObjectModelProvider extends ModelProvider {

	private static Pattern vertexPattern = Pattern.compile("(v( (\\-)?\\d+\\.\\d+){3,4} *\\n)|(v( (\\-)?\\d+\\.\\d+){3,4} *$)");
	private static Pattern vertexNormalPattern = Pattern.compile("(vn( (\\-)?\\d+\\.\\d+){3,4} *\\n)|(vn( (\\-)?\\d+\\.\\d+){3,4} *$)");
	private static Pattern textureCoordinatePattern = Pattern.compile("(vt( (\\-)?\\d+\\.\\d+){2,3} *\\n)|(vt( (\\-)?\\d+\\.\\d+){2,3} *$)");
	// According to the official Wavefront OBJ specification, a face can have an unlimited amount of vertices
	private static Pattern face_V_VT_VN_Pattern = Pattern.compile("(f( \\d+/\\d+/\\d+){3,} *\\n)|(f( \\d+/\\d+/\\d+){3,} *$)");
	private static Pattern face_V_VT_Pattern = Pattern.compile("(f( \\d+/\\d+){3,} *\\n)|(f( \\d+/\\d+){3,} *$)");
	private static Pattern face_V_VN_Pattern = Pattern.compile("(f( \\d+//\\d+){3,} *\\n)|(f( \\d+//\\d+){3,} *$)");
	private static Pattern face_V_Pattern = Pattern.compile("(f( \\d+){3,} *\\n)|(f( \\d+){3,} *$)");
	private static Pattern subModelPattern = Pattern.compile("([go]([^\\\\ ]*+)*\\n)|([go]( [^\\\\ ]*+) *$)");
	private static Matcher globalMatcher;
	//A map of all models generated with their names
	private final MeshModel model = new MeshModel();
	private MeshModel currentModel = null;
	private ArrayList<Vector3D> vertices = new ArrayList<>();
	private ArrayList<Vector2D> textureCoordinates = new ArrayList<>();
	private ArrayList<Vector3D> vertexNormals = new ArrayList<>();

	/**
	 * Creates new ModelProvider
	 * @param domain dolain of the assets.
	 * @param name name of the model.
	 */
	public WavefrontObjectModelProvider(String domain, String name) {
		super(domain, name);
	}

	@Override
	public void load(InputStream stream) {
		String currentLine;
		int lineCount = 0;
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(stream))){

			while ((currentLine = reader.readLine()) != null) {
				lineCount++;
				currentLine = currentLine.replaceAll("\\s+", " ").trim();
				if (currentLine.startsWith("#") || currentLine.isEmpty()) {
					continue;
				} else if (currentLine.startsWith("v ")) {
					Vector3D vertex = parseToVertex(currentLine, lineCount);
					if (vertex != null) {
						vertices.add(vertex);
					}
				} else if (currentLine.startsWith("vt ")) {
					Vector2D textureCoordinate = parseTextureCoordinate(currentLine, lineCount);
					if (textureCoordinate != null) {
						textureCoordinates.add(textureCoordinate);
					}
				} else if (currentLine.startsWith("vn ")) {
					Vector3D vertexNormal = parseToVertexNormal(currentLine, lineCount);
					if (vertexNormal != null) {
						vertexNormals.add(vertexNormal);
					}
				} else if (currentLine.startsWith("vp ")) {
					// TODO: Parameter space vertices
					Game.logger().warn("Model {} uses parameter space vertices", this.name);
				} else if (currentLine.startsWith("f ")) {
					if (currentModel == null) {
						currentModel = new MeshModel("Default");
					}

					Face face = parseToFace(currentLine, lineCount);
					if (face != null) {
						currentModel.faces.add(face);
					}
				} else if (currentLine.startsWith("g ") | currentLine.startsWith("o ")) {
					MeshModel subModel = parseToModel(currentLine, lineCount);
					if (subModel != null) {
						if (currentModel != null) {
							model.children.add(currentModel);
						}
					}
					currentModel = subModel;
				}
			}
			model.children.add(currentModel);
		} catch (IOException | UnsupportedOperationException e) {
			throw new RenderException("Model " + this.name + " could not be read", e);
		} finally {
			this.cleanUp();
		}
	}

	private void cleanUp() {
		this.currentModel = null;
		this.vertices.clear();
		this.textureCoordinates.clear();
	}

	@Override
	public MeshModel getModel() {
		return model.clone();
	}

	@Override
	public String getType() {
		return "obj";
	}

	private Vector3D parseToVertex(String line, int lineNumber) {
		if (isValid(line, vertexPattern)) {
			line = line.substring(line.indexOf(' ') + 1);
			String[] tokens = line.split(" ");
			try {
				if (tokens.length == 2) {
					return new Vector3D(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), 0);
				} else if (tokens.length == 3) {
					return new Vector3D(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
				}
			} catch (NumberFormatException e) {
				throw new RenderException(String.format("Number formatting error at line %d", lineNumber), e);
			}
		} else {
			throw new RenderException("Error parsing entry ('" + line + "'" + ", line " + lineNumber + ") in model '" + this.name + "' - Incorrect format");
		}
		return null;
	}

	private Vector2D parseTextureCoordinate(String line, int lineNumber) {
		if (isValid(line, textureCoordinatePattern)) {
			line = line.substring(line.indexOf(' ') + 1);
			String[] tokens = line.split(" ");
			try {
				if (tokens.length >= 2) {
					return new Vector2D(Float.parseFloat(tokens[0]), 1 - Float.parseFloat(tokens[1]));
				}
			} catch (NumberFormatException e) {
				throw new RenderException(String.format("Number formatting error at line %d", lineNumber), e);
			}
		} else {
			throw new RenderException("Error parsing entry ('" + line + "'" + ", line " + lineNumber + ") in model '" + this.name + "' - Incorrect format");
		}
		return null;
	}

	private Vector3D parseToVertexNormal(String line, int lineNumber) {
		if (isValid(line, vertexNormalPattern)) {
			line = line.substring(line.indexOf(' ') + 1);
			String[] tokens = line.split(" ");
			try {
				if (tokens.length == 3) {
					// According to the official Wavefront OBJ specification, vertex normals might not be normalized.
					return new Vector3D(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2])).normalize();
				}
			} catch (NumberFormatException e) {
				throw new RenderException(String.format("Number formatting error at line %d", lineNumber), e);
			}
		} else {
			throw new RenderException("Error parsing entry ('" + line + "'" + ", line " + lineNumber + ") in model '" + this.name + "' - Incorrect format");
		}
		return null;
	}

	private Face parseToFace(String line, int lineNumber) {
		Face face = null;
		if (isValid(line, face_V_VT_VN_Pattern) || isValid(line, face_V_VT_Pattern) || isValid(line, face_V_VN_Pattern) || isValid(line, face_V_Pattern)) {
			face = new Face();
			String trimmedLine = line.substring(line.indexOf(' ') + 1);
			String[] tokens = trimmedLine.split(" ");
			String[] subTokens = null;

			// f v1/vt1/vn1 v2/vt2/vn2 v3/vt3/vn3 ...
			if (isValid(line, face_V_VT_VN_Pattern)) {
				for (int i = 0; i < tokens.length; ++i) {
					subTokens = tokens[i].split("/");
					face.drawVertex(new Vertex(getVertex(Integer.parseInt(subTokens[0])), getTexVec(Integer.parseInt(subTokens[1])), getNormal(Integer.parseInt(subTokens[2]))));
				}
				face.normal = Vector3DUtil.calculateNormal(face);
			}
			// f v1/vt1 v2/vt2 v3/vt3 ...
			else if (isValid(line, face_V_VT_Pattern)) {
				for (int i = 0; i < tokens.length; ++i) {
					subTokens = tokens[i].split("/");
					face.drawVertex(new Vertex(getVertex(Integer.parseInt(subTokens[0])), getTexVec(Integer.parseInt(subTokens[1]))));
				}
				face.normal = Vector3DUtil.calculateNormal(face);
			}
			// f v1//vn1 v2//vn2 v3//vn3 ...
			else if (isValid(line, face_V_VN_Pattern)) {
				for (int i = 0; i < tokens.length; ++i) {
					subTokens = tokens[i].split("//");
					face.drawVertex(new Vertex(getVertex(Integer.parseInt(subTokens[0])), Vector2D.ZERO, getNormal(Integer.parseInt(subTokens[1]))));
				}
				face.normal = Vector3DUtil.calculateNormal(face);
			}
			// f v1 v2 v3 ...
			else if (isValid(line, face_V_Pattern)) {
				for (int i = 0; i < tokens.length; ++i) {
					face.drawVertex(new Vertex(getVertex(Integer.parseInt(tokens[i])), Vector2D.ZERO));
				}
				face.normal = Vector3DUtil.calculateNormal(face);
			} else {
				throw new RenderException("Error parsing entry ('" + line + "'" + ", line " + lineNumber + ") in model '" + this.name + "' - Incorrect format");
			}
		} else {
			throw new RenderException("Error parsing entry ('" + line + "'" + ", line " + lineNumber + ") in model '" + this.name + "' - Incorrect format");
		}
		return face;
	}

	private MeshModel parseToModel(String line, int lineNumber) {
		if (isValid(line, subModelPattern)) {
			String trimmedLine = line.substring(line.indexOf(' ') + 1);
			if (!trimmedLine.isEmpty()) {
				return new MeshModel(trimmedLine);
			}
		} else {
			throw new RenderException("Error parsing entry ('" + line + "'" + ", line " + lineNumber + ") in model '" + this.name + "' - Incorrect format");
		}
		return null;
	}

	/**
	 * Verifies that the given line from the model file is a valid for a given pattern
	 *
	 * @param line the line being validated
	 * @param pattern the validator
	 * @return true if the line is a valid for a given pattern, false otherwise
	 */
	private boolean isValid(String line, Pattern pattern) {
		if (globalMatcher != null) {
			globalMatcher.reset();
		}

		globalMatcher = pattern.matcher(line);
		return globalMatcher.matches();
	}

	private Vector3D getVertex(int index) {
		try {
			return vertices.get(index < 0 ? index + textureCoordinates.size() : index - 1);
		} catch (IndexOutOfBoundsException e) {
			System.err.println("[OBJ]: Can't get vertex " + index + "! Is this model corrupted?");
			return Vector3D.ZERO;
		}
	}

	private Vector2D getTexVec(int index) {
		try {
			return textureCoordinates.get(index < 0 ? index + textureCoordinates.size() : index - 1);
		} catch (IndexOutOfBoundsException e) {
			System.err.println("[OBJ]: Can't get textureCoordinate " + index + "! Is this model corrupted?");
			return Vector2D.ZERO;
		}
	}

	private Vector3D getNormal(int index) {
		try {
			return vertexNormals.get(index < 0 ? index + textureCoordinates.size() : index - 1);
		} catch (IndexOutOfBoundsException e) {
			System.err.println("[OBJ]: Can't get vertexNormal " + index + "! Is this model corrupted?");
			return Vector3D.ZERO;
		}
	}
}
