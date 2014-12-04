package test;

import gl.Vertex;

import java.io.PrintWriter;
import java.util.ArrayList;

import run.Final_Main;
import utils.ComUtils;

public class Test
{
	public static void main(String[] args) throws Exception
	{
		PrintWriter pw = new PrintWriter("M14h.asc");
		ArrayList<Vertex[]> triList = ComUtils.readModelFile("data/fromRin/M14h.obj");
		for (Vertex[] tri : triList)
		{
			pw.println("triangle");
			for (Vertex vertex : tri)
				pw.println(vertex.x + "\t" + vertex.y + "\t" + vertex.z + "\t" + vertex.U + "\t" + vertex.V + "\t" + vertex.norm.x + "\t" + vertex.norm.y
						+ "\t" + vertex.norm.z);
		}
		pw.close();
	}
}