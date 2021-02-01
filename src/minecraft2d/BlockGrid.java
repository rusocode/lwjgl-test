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

	// Matriz de bloques
	private Block[][] blocks = new Block[BLOCKS_WIDTH][BLOCKS_HEIGHT];

	// Crea un nuevo bloque para cada posicion de la matriz
	public BlockGrid() {
		for (int x = 0; x < BLOCKS_WIDTH; x++) {
			for (int y = 0; y < BLOCKS_HEIGHT; y++) {
				blocks[x][y] = new Block(BlockType.AIR, x * BLOCK_SIZE, y * BLOCK_SIZE);
			}
		}
	}

	public void setAt(int x, int y, BlockType type) {
		blocks[x][y] = new Block(type, x * BLOCK_SIZE, y * BLOCK_SIZE); // FIXME hace falta crear un nuevo bloque o lo reemplazo?
	}

	public Block getAt(int x, int y) {
		return blocks[x][y];
	}

	// Guarda el estado del juego en formato xml
	public void save(File file) {

		Document document = new Document();
		Element root = new Element("blocks"); // Etiqueta raiz (bloques)
		document.setRootElement(root); // Establece el elemento raiz

		for (int x = 0; x < BLOCKS_WIDTH; x++) {
			for (int y = 0; y < BLOCKS_HEIGHT; y++) {

				Element block = new Element("block"); // Segunda etiqueta (bloque)

				// Establece los valores para los atributos (x, y, type) en la etiqueta bloque
				block.setAttribute("x", String.valueOf((int) (blocks[x][y].getX() / BLOCK_SIZE)));
				block.setAttribute("y", String.valueOf((int) (blocks[x][y].getY() / BLOCK_SIZE)));
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

	// Dibuja los bloques de cada posicion
	public void draw() {
		for (int x = 0; x < BLOCKS_WIDTH - 1; x++) { // Hace falta el -1?
			for (int y = 0; y < BLOCKS_HEIGHT - 1; y++) {
				blocks[x][y].draw();
			}
		}
	}

	public void clear() {
		for (int x = 0; x < BLOCKS_WIDTH; x++) {
			for (int y = 0; y < BLOCKS_HEIGHT; y++) {
				blocks[x][y] = new Block(BlockType.AIR, x * BLOCK_SIZE, y * BLOCK_SIZE);
			}
		}
	}

}
