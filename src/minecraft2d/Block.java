package minecraft2d;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static minecraft2d.World.BLOCK_SIZE;

import static org.lwjgl.opengl.GL11.*;

public final class Block {

	// Por defecto queda seleccionado el bloque de aire
	private BlockType type = BlockType.AIR;
	// Ubicacion del bloque
	private float x;
	private float y;

	private Texture texture;

	public Block(BlockType type, float x, float y) {
		this.type = type;
		this.x = x;
		this.y = y;
		try {
			this.texture = TextureLoader.getTexture("PNG", new FileInputStream(new File(type.location)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public BlockType getType() {
		return type;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void bind() {
		texture.bind();
	}

	public void draw() {
		
		glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
		glPushMatrix();
		glTranslatef(x, y, 0);
		
		// Crea un bloque 
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(0, 0);
		glTexCoord2f(1, 0);
		glVertex2f(World.BLOCK_SIZE, 0);
		glTexCoord2f(1, 1);
		glVertex2f(World.BLOCK_SIZE, World.BLOCK_SIZE);
		glTexCoord2f(0, 1);
		glVertex2f(0, World.BLOCK_SIZE);
		glEnd();
		
		glPopMatrix();
	}

}
