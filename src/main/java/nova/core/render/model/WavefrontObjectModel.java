package nova.core.render.model;

import nova.core.render.RenderException;
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
public class WavefrontObjectModel extends ModelProvider {

	private static Pattern vertexPattern = Pattern.compile("(v( (\\-)?\\d+\\.\\d+){3,4} *\\n)|(v( (\\-)?\\d+\\.\\d+){3,4} *$)");
	private static Pattern vertexNormalPattern = Pattern.compile("(vn( (\\-)?\\d+\\.\\d+){3,4} *\\n)|(vn( (\\-)?\\d+\\.\\d+){3,4} *$)");
	private static Pattern textureCoordinatePattern = Pattern.compile("(vt( (\\-)?\\d+\\.\\d+){2,3} *\\n)|(vt( (\\-)?\\d+\\.\\d+){2,3} *$)");
	private static Pattern face_V_VT_VN_Pattern = Pattern.compile("(f( \\d+/\\d+/\\d+){3,4} *\\n)|(f( \\d+/\\d+/\\d+){3,4} *$)");
	private static Pattern face_V_VT_Pattern = Pattern.compile("(f( \\d+/\\d+){3,4} *\\n)|(f( \\d+/\\d+){3,4} *$)");
	private static Pattern face_V_VN_Pattern = Pattern.compile("(f( \\d+//\\d+){3,4} *\\n)|(f( \\d+//\\d+){3,4} *$)");
	private static Pattern face_V_Pattern = Pattern.compile("(f( \\d+){3,4} *\\n)|(f( \\d+){3,4} *$)");
	private static Pattern subModelPattern = Pattern.compile("([go]([^\\\\ ]*+)*\\n)|([go]( [^\\\\ ]*+) *$)");
	private static Matcher globalMatcher;
	//A map of all models generated with their names
	private final Model model = new Model();
	private Model currentModel = null;
	private ArrayList<Vector3D> vertices = new ArrayList<>();
	private ArrayList<Vector2D> textureCoordinates = new ArrayList<>();

	public WavefrontObjectModel(String domain, String name) {
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
				stream.close();
			} catch (IOException e) {}
			this.cleanUp();
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

	private Vector3D parseToVertex(String line, int lineNumber) {
		if (isValid(line, vertexPattern)) {
			line = line.substring(line.indexOf(" ") + 1);
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
			throw new RenderException("Error parsing entry ('" + line + "'" + ", line " + lineNumber + ") in model '" + name + "' - Incorrect format");
		}
		return null;
	}

	private Vector2D parseTextureCoordinate(String line, int lineNumber) {
		if (isValid(line, textureCoordinatePattern)) {
			line = line.substring(line.indexOf(" ") + 1);
			String[] tokens = line.split(" ");
			try {
				if (tokens.length >= 2) {
					return new Vector2D(Float.parseFloat(tokens[0]), 1 - Float.parseFloat(tokens[1]));
				}
			} catch (NumberFormatException e) {
				throw new RenderException(String.format("Number formatting error at line %d", lineNumber), e);
			}
		} else {
			throw new RenderException("Error parsing entry ('" + line + "'" + ", line " + lineNumber + ") in model '" + name + "' - Incorrect format");
		}
		return null;
	}

	private Vector3D parseToVertexNormal(String line, int lineNumber) {
		if (isValid(line, vertexNormalPattern)) {
			line = line.substring(line.indexOf(" ") + 1);
			String[] tokens = line.split(" ");
			try {
				if (tokens.length == 3) {
					return new Vector3D(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
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
					face.drawVertex(new Vertex(vertices.get(Integer.parseInt(subTokens[0]) - 1), Vector2D.ZERO));
				}
				face.normal = calculateNormal(face);
			}
			// f v1 v2 v3 ...
			else if (isValid(line, face_V_Pattern)) {
				for (int i = 0; i < tokens.length; ++i) {
					face.drawVertex(new Vertex(vertices.get(Integer.parseInt(tokens[i]) - 1), Vector2D.ZERO));
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
			if (!trimmedLine.isEmpty()) {
				return new Model(trimmedLine);
			}
		} else {
			throw new RenderException("Error parsing entry ('" + line + "'" + ", line " + lineNumber + ") in model '" + name + "' - Incorrect format");
		}
		return null;
	}

	private Vector3D calculateNormal(Face face) {
		Vertex firstEntry = face.vertices.get(0);
		Vertex secondEntry = face.vertices.get(1);
		Vertex thirdEntry = face.vertices.get(1);
		Vector3D v1 = new Vector3D(secondEntry.vec.getX() - firstEntry.vec.getX(), secondEntry.vec.getY() - firstEntry.vec.getY(), secondEntry.vec.getZ() - firstEntry.vec.getZ());
		Vector3D v2 = new Vector3D(thirdEntry.vec.getX() - firstEntry.vec.getX(), thirdEntry.vec.getY() - firstEntry.vec.getY(), thirdEntry.vec.getZ() - firstEntry.vec.getZ());

		return v1.crossProduct(v2).normalize();
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

	private Vector2D getTexVec(int index) {
		try {
			return textureCoordinates.get(index);
		} catch (IndexOutOfBoundsException e) {
			System.err.println("[OBJ]: Can't get textureCoordinate " + index + "! Is this model corrupted?");
			return Vector2D.ZERO;
		}
	}

}
