package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import myGL.Coord;
import myGL.Vertex;

import com.obj.WavefrontObject;

public class TestOBJ_Import
{
	// TODO remain unfinished
	// Read obj file
	public static ArrayList<Vertex[]> readOBJFile(String inputFileName) throws Exception
	{
		ArrayList<Vertex[]> triList = new ArrayList<Vertex[]>();
		BufferedReader br = new BufferedReader(new FileReader(inputFileName));

		com.obj.WavefrontObject obj = new com.obj.WavefrontObject("data/obj/m1.obj");
		ArrayList<com.obj.Face> faces = obj.getGroups().get(0).getFaces();

		for (com.obj.Face face : faces)
		{
			Vertex[] tri = new Vertex[3];
			com.obj.Vertex[] vertexes = face.getVertices();
			com.obj.Vertex[] normals = face.getNormals();
			com.obj.TextureCoordinate[] textures = face.getTextures();
			for (int i = 0; i < tri.length; i++)
			{
				
				float x, y, z, w = 1, nx, ny, nz, u, v;
				x = vertexes[i].getX();
				y = vertexes[i].getY();
				z = vertexes[i].getZ();
				nx = normals[i].getX();
				ny = normals[i].getY();
				nz = normals[i].getZ();
				u = textures[i].getU();
				v = textures[i].getV();
				tri[i] = new Vertex(x, y, z, w, new Coord(nx, ny, nz, 0), u, v);
			}
			triList.add(tri);
		}

		br.close();

		return triList;
	}
	
	public static void main(String[] args)
	{
		WavefrontObject obj = new WavefrontObject("data/obj/m1.obj");
		System.out.println(obj.getGroups().get(0).getFaces().get(0).getVertices());
	}
}