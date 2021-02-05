package minecraft2d;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static minecraft2d.World.*;

class BlockGrid {

	// Matriz de Block sin inicializar
	private Block[][] blocks;

	// Usar ArrayList para manejar las dimensiones dinamicas de la ventana
	// private ArrayList<Block> blocks;

	public BlockGrid() {

		// Crea una matriz de 300 posiciones
		blocks = new Block[columnas][filas];

		for (int x = 0; x < columnas; x++) {
			for (int y = 0; y < filas; y++) {
				// Crea un bloque y lo agrega a la matriz
				blocks[x][y] = new Block(BlockType.AIR, x * BLOCK_SIZE, y * BLOCK_SIZE);
			}
		}

	}

	public void setAt(BlockType type, int x, int y) {
		blocks[x][y] = new Block(type, x * BLOCK_SIZE, y * BLOCK_SIZE);
	}

	public Block getAt(int x, int y) {
		return blocks[x][y];
	}

	// Guarda el estado del juego en formato xml
	public void save(File file) {

		Document document = new Document();
		Element root = new Element("blocks"); // Etiqueta raiz (bloques)
		document.setRootElement(root); // Establece el elemento raiz

		for (int x = 0; x < columnas; x++) {
			for (int y = 0; y < filas; y++) {

				Element block = new Element("block"); // Segunda etiqueta (bloque)

				// Establece los valores para los atributos (x, y, type) en la etiqueta bloque
				block.setAttribute("x", String.valueOf(x));
				block.setAttribute("y", String.valueOf(y));
				block.setAttribute("type", String.valueOf(blocks[x][y].getType()));

				// Agrega la etiqueta bloque a la etiqueta bloques
				root.addContent(block);
			}
		}

		XMLOutputter xmlOutputter = new XMLOutputter();

		try {
			// Crea el archivo xml en la carpeta del proyecto
			xmlOutputter.output(document, new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Carga el estado del juego
	public void load(File file) {

		try {

			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(file);
			Element root = document.getRootElement();

			for (Object block : root.getChildren()) { // Convierte el tipo de datos Element a Object

				// Convierte el tipo de datos Object a Element (raro)
				Element e = (Element) block;

				// Crea el bloque con los valores obtenidos del xml
				int x = Integer.parseInt(e.getAttributeValue("x"));
				int y = Integer.parseInt(e.getAttributeValue("y"));

				blocks[x][y] = new Block(BlockType.valueOf(e.getAttributeValue("type")), x * BLOCK_SIZE, y * BLOCK_SIZE);

			}

		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Dibuja las textura en cada posicion de la matriz
	public void draw() {
		for (int x = 0; x < columnas; x++) {
			for (int y = 0; y < filas; y++) {
				blocks[x][y].draw();
			}
		}
	}

	public void clear() {
		for (int x = 0; x < columnas; x++) {
			for (int y = 0; y < filas; y++) {
				blocks[x][y] = new Block(BlockType.AIR, x * BLOCK_SIZE, y * BLOCK_SIZE);
			}
		}
	}

}
