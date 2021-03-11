package minecraft2d;

public enum BlockType {

	// Almacena la ubicacion de las imagenes con el tipo de bloque
	AIR("assets/textures/air.png"), GRASS("assets/textures/grass.png"), DIRT("assets/textures/dirt.png"), STONE("assets/textures/stone.png"),
	BRICK("assets/textures/brick.png");

	// La textura brick128 tiene mejor calidad por la cantidad de pixeles, pero ocupa mas memoria

	public final String location;

	BlockType(String location) {
		this.location = location;
	}

}
