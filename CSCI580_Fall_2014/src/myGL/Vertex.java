package myGL;

public class Vertex extends Coord
{
	public Coord norm;
	public float U;
	public float V;

	public Vertex()
	{
		super();
		norm = new Coord();
		U = 0;
		V = 0;
	}

	public Vertex(float x, float y, float z, float w, Coord norm, float U, float V)
	{
		super(x, y, z, w);
		this.norm = norm;
		this.U = U;
		this.V = V;
	}
}