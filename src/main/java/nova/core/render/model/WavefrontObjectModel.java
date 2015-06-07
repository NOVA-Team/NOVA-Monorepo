package nova.core.render.model;

import nova.core.render.RenderException;
import nova.core.util.transform.vector.Vector2d;
import nova.core.util.transform.vector.Vector3d;

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
public class WavefrontObjectModel extends ModelProvider {

	private static Pattern vertexPattern = Pattern.compile("(v( (\\-){0,1}\\d+\\.\\d+){3,4} *\\n)|(v( (\\-){0,1}\\d+\\.\\d+){3,4} *$)");
	private static Pattern vertexNormalPattern = Pattern.compile("(vn( (\\-){0,1}\\d+\\.\\d+){3,4} *\\n)|(vn( (\\-){0,1}\\d+\\.\\d+){3,4} *$)");
	private static Pattern textureCoordinatePattern = Pattern.compile("(vt( (\\-){0,1}\\d+\\.\\d+){2,3} *\\n)|(vt( (\\-){0,1}\\d+\\.\\d+){2,3} *$)");
	private static Pattern face_V_VT_VN_Pattern = Pattern.compile("(f( \\d+/\\d+/\\d+){3,4} *\\n)|(f( \\d+/\\d+/\\d+){3,4} *$)");
	private static Pattern face_V_VT_Pattern = Pattern.compile("(f( \\d+/\\d+){3,4} *\\n)|(f( \\d+/\\d+){3,4} *$)");
	private static Pattern face_V_VN_Pattern = Pattern.compile("(f( \\d+//\\d+){3,4} *\\n)|(f( \\d+//\\d+){3,4} *$)");
	private static Pattern face_V_Pattern = Pattern.compile("(f( \\d+){3,4} *\\n)|(f( \\d+){3,4} *$)");
	private static Pattern subModelPattern = Pattern.compile("([go]([^\\\\ ]*+)*\\n)|([go]( [^\\\\ ]*+) *$)");
	private static Matcher globalMatcher;
	//A map of all models generated with their names
	private final Model model = new Model();
	private Model currentModel = null;
	private ArrayList<Vector3d> vertices = new ArrayList<>();
	private ArrayList<Vector2d> textureCoordinates = new ArrayList<>();

	public WavefrontObjectModel(String domain, String name) {
		super(domain, name);
	}

	@Override
	public void load(InputStream stream) {

		BufferedReader reader = null;
		String currentLine;
		int lineCount = 0;
		try {
			reader = new BufferedReader(new InputStreamReader(stream));
			while ((currentLine = reader.readLine()) != null) {
				lineCount++;
				currentLine = currentLine.replaceAll("\\s+", " ").trim();
				if (currentLine.startsWith("#") || currentLine.length() == 0) {
					continue;
				} else if (currentLine.startsWith("v ")) {
					Vector3d vertex = parseToVertex(currentLine, lineCount);
					if (vertex != null) {
						vertices.add(vertex);
					}
				} else if (currentLine.startsWith("vt ")) {
					Vector2d textureCoordinate = parseTextureCoordinate(currentLine, lineCount);
					if (textureCoordinate != null) {
						textureCoordinates.add(textureCoordinate);
					}
				} else if (currentLine.startsWith("f ")) {
					if (currentModel == null) {
						currentModel = new Model("Default");
					}

					Face face = parseToFace(currentLine, lineCount);
					if (face != null) {
						currentModel.faces.add(face);
					}
				} else if (currentLine.startsWith("g ") | currentLine.startsWith("o ")) {
					Model subModel = parseToModel(currentLine, lineCount);
					if (subModel != null) {
						if (currentModel != null) {
							model.children.add(currentModel);
						}
					}
					currentModel = subModel;
				}
			}
			model.children.add(currentModel);
		} catch (IOException e) {
			throw new RenderException("Model " + name + " could not be read", e);
		} finally {
			try {
				reader.close();
				stream.close();
				this.cleanUp();
			} catch (IOException e) {

			}
		}
	}

	private void cleanUp() {
		this.currentModel = null;
		this.vertices.clear();
		this.textureCoordinates.clear();
	}

	@Override
	public Model getModel() {
		return model.clone();
	}

	@Override
	public String getType() {
		return "obj";
	}

	private Vector3d parseToVertex(String line, int lineNumber) {
		if (isValid(line, vertexPattern)) {
			line = line.substring(line.indexOf(" ") + 1);
			String[] tokens = line.split(" ");
			try {
				if (tokens.length == 2) {
					return new Vector3d(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), 0);
				} else if (tokens.length == 3) {
					return new Vector3d(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
				}
			} catch (NumberFormatException e) {
				throw new RenderException(String.format("Number formatting error at line %d", lineNumber), e);
			}
		} else {
			throw new RenderException("Error parsing entry ('" + line + "'" + ", line " + lineNumber + ") in model '" + name + "' - Incorrect format");
		}
		return null;
	}

	private Vector2d parseTextureCoordinate(String line, int lineNumber) {
		if (isValid(line, textureCoordinatePattern)) {
			line = line.substring(line.indexOf(" ") + 1);
			String[] tokens = line.split(" ");
			try {
				if (tokens.length >= 2) {
					return new Vector2d(Float.parseFloat(tokens[0]), 1 - Float.parseFloat(tokens[1]));
				}
			} catch (NumberFormatException e) {
				throw new RenderException(String.format("Number formatting error at line %d", lineNumber), e);
			}
		} else {
			throw new RenderException("Error parsing entry ('" + line + "'" + ", line " + lineNumber + ") in model '" + name + "' - Incorrect format");
		}
		return null;
	}

	private Vector3d parseToVertexNormal(String line, int lineNumber) {
		if (isValid(line, vertexNormalPattern)) {
			line = line.substring(line.indexOf(" ") + 1);
			String[] tokens = line.split(" ");
			try {
				if (tokens.length == 3) {
					return new Vector3d(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
				}
			} catch (NumberFormatException e) {
				throw new RenderException(String.format("Number formatting error at line %d", lineNumber), e);
			}
		} else {
			throw new RenderException("Error parsing entry ('" + line + "'" + ", line " + lineNumber + ") in model '" + name + "' - Incorrect format");
		}
		return null;
	}

	private Face parseToFace(String line, int lineNumber) {
		Face face = null;
		if (isValid(line, face_V_VT_VN_Pattern) || isValid(line, face_V_VT_Pattern) || isValid(line, face_V_VN_Pattern) || isValid(line, face_V_Pattern)) {
			face = new Face();
			String trimmedLine = line.substring(line.indexOf(" ") + 1);
			String[] tokens = trimmedLine.split(" ");
			String[] subTokens = null;

			// f v1/vt1/vn1 v2/vt2/vn2 v3/vt3/vn3 ...
			if (isValid(line, face_V_VT_VN_Pattern)) {
				for (int i = 0; i < tokens.length; ++i) {
					subTokens = tokens[i].split("/");
					face.drawVertex(new Vertex(vertices.get(Integer.parseInt(subTokens[0]) - 1), getTexVec(Integer.parseInt(subTokens[1]) - 1)));
				}
				face.normal = calculateNormal(face);
			}
			// f v1/vt1 v2/vt2 v3/vt3 ...
			else if (isValid(line, face_V_VT_Pattern)) {
				for (int i = 0; i < tokens.length; ++i) {
					subTokens = tokens[i].split("/");
					face.drawVertex(new Vertex(vertices.get(Integer.parseInt(subTokens[0]) - 1), getTexVec(Integer.parseInt(subTokens[1]) - 1)));
				}
				face.normal = calculateNormal(face);
			}
			// f v1//vn1 v2//vn2 v3//vn3 ...
			else if (isValid(line, face_V_VN_Pattern)) {
				for (int i = 0; i < tokens.length; ++i) {
					subTokens = tokens[i].split("//");
					face.drawVertex(new Vertex(vertices.get(Integer.parseInt(subTokens[0]) - 1), Vector2d.zero));
				}
				face.normal = calculateNormal(face);
			}
			// f v1 v2 v3 ...
			else if (isValid(line, face_V_Pattern)) {
				for (int i = 0; i < tokens.length; ++i) {
					face.drawVertex(new Vertex(vertices.get(Integer.parseInt(tokens[i]) - 1), Vector2d.zero));
				}
				face.normal = calculateNormal(face);
			} else {
				throw new RenderException("Error parsing entry ('" + line + "'" + ", line " + lineNumber + ") in model '" + name + "' - Incorrect format");
			}
		} else {
			throw new RenderException("Error parsing entry ('" + line + "'" + ", line " + lineNumber + ") in model '" + name + "' - Incorrect format");
		}
		return face;
	}

	private Model parseToModel(String line, int lineNumber) {
		if (isValid(line, subModelPattern)) {
			String trimmedLine = line.substring(line.indexOf(" ") + 1);
			if (trimmedLine.length() > 0) {
				return new Model(trimmedLine);
			}
		} else {
			throw new RenderException("Error parsing entry ('" + line + "'" + ", line " + lineNumber + ") in model '" + name + "' - Incorrect format");
		}
		return null;
	}

	private Vector3d calculateNormal(Face face) {
		Vertex firstEntry = face.vertices.get(0);
		Vertex secondEntry = face.vertices.get(1);
		Vertex thirdEntry = face.vertices.get(1);
		Vector3d v1 = new Vector3d(secondEntry.vec.x - firstEntry.vec.x, secondEntry.vec.y - firstEntry.vec.y, secondEntry.vec.z - firstEntry.vec.z);
		Vector3d v2 = new Vector3d(thirdEntry.vec.x - firstEntry.vec.x, thirdEntry.vec.y - firstEntry.vec.y, thirdEntry.vec.z - firstEntry.vec.z);

		return v1.cross(v2).normalize();
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

	private Vector2d getTexVec(int index) {
		try {
			return textureCoordinates.get(index);
		} catch (IndexOutOfBoundsException e) {
			System.err.println("[OBJ]: Can't get textureCoordinate " + index + "! Is this model corrupted?");
			return Vector2d.zero;
		}
	}

}
