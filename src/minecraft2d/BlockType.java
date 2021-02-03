package minecraft2d;

public enum BlockType {

	// Almacena la ubicacion de las imagenes con el tipo de bloque
	AIR("res/img/air.png"), GRASS("res/img/grass.png"), DIRT("res/img/dirt.png"), STONE("res/img/stone.png"), BRICK("res/img/brick.png");

	// La textura brick128 tiene mejor calidad por la cantidad de pixeles, pero ocupa mas memoria

	public final String location;

	BlockType(String location) {
		this.location = location;
	}

}
