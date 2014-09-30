package test;

import utils.ComUtils;

public class TestMatrix
{
	public static void main(String[] args) throws Exception
	{
		// float[][][] verts = { { { 2.4f }, { 2.25f }, { 1.0f }, { 1.0f } }, { { -1.2915f }, { 1.25f }, { 0.5495f }, { 1.0f } },
		// { { 0.273482f }, { -0.32828f }, { 2.541834f }, { 1.0f } } };
		// float[][] Xsp = { { 128.0f, 0.0f, 0.0f, 128.0f }, { 0.0f, -128.0f, 0.0f, 128.0f }, { 0.0f, 0.0f, 1.0f, 0.0f }, { 0.0f, 0.0f, 0.0f, 1.0f } };
		// float[][] Xpi = { { 1.0f, 0.0f, 0.0f, 0.0f }, { 0.0f, 1.0f, 0.0f, 0.0f }, { 0.0f, 0.0f, 1.0f, 0.0f }, { 0.0f, 0.0f, 3.1715946f, 1.0f } };
		// float[][] Xiw = { { 0.7071068f, 0.23570228f, 0.6666667f, -10.0f }, { 0.0f, 0.94280905f, -0.33333334f, 5.0f },
		// { -0.7071068f, 0.23570228f, 0.6666667f, -10.0f }, { 0.0f, 0.0f, 0.0f, 1.0f } };
		// float[][] Xt = { { 1f, 0f, 0f, 0f }, { 0f, 1f, 0f, -3.25f }, { 0f, 0f, 1f, 3.5f, 0f }, { 0f, 0f, 0f, 1f } };
		// float[][] Xs = { { 3.25f, 0f, 0f, 0f }, { 0f, 3.25f, 0f, 0f }, { 0f, 0f, 3.25f, 0f }, { 0f, 0f, 0f, 1f } };
		// float[][] Xy = { { 0.866f, 0.0f, -0.5f, 0.0f }, { 0.0f, 1.0f, 0.0f, 0.0f }, { 0.5f, 0.0f, 0.866f, 0.0f }, { 0.0f, 0.0f, 0.0f, 1.0f } };
		// float[][] Xx = { { 1.0f, 0.0f, 0.0f, 0.0f }, { 0.0f, 0.7071f, 0.7071f, 0.0f }, { 0.0f, -0.7071f, 0.7071f, 0.0f }, { 0.0f, 0.0f, 0.0f, 1.0f } };
		// for (int i = 0; i < verts.length; i++)
		// {
		// verts[i] = ComUtils.Multiply(Xx, verts[i]);
		// verts[i] = ComUtils.Multiply(Xy, verts[i]);
		// verts[i] = ComUtils.Multiply(Xs, verts[i]);
		// verts[i] = ComUtils.Multiply(Xt, verts[i]);
		// verts[i] = ComUtils.Multiply(Xiw, verts[i]);
		// verts[i] = ComUtils.Multiply(Xpi, verts[i]);
		// verts[i] = ComUtils.Multiply(Xsp, verts[i]);
		// verts[i] = ComUtils.Multiply(1 / verts[i][3][0], verts[i]);
		// for (int j = 0; j < verts[i].length - 1; j++)
		// System.out.print("" + verts[i][j][0] + "\t");
		// System.out.println();
		// }

		float[][][] verts = { { { 1f }, { 0f }, { 0f }, { 1f } }, { { 0f }, { 1f }, { 0f }, { 1.0f } }, { { 0f }, { 0f }, { 1f }, { 1.0f } } };
		float[][] Xsp = { { 128.0f, 0.0f, 0.0f, 128.0f }, { 0.0f, -128.0f, 0.0f, 128.0f }, { 0.0f, 0.0f, 1.0f, 0.0f }, { 0.0f, 0.0f, 0.0f, 1.0f } };
		float[][] Xpi = { { 1.0f, 0.0f, 0.0f, 0.0f }, { 0.0f, 1.0f, 0.0f, 0.0f }, { 0.0f, 0.0f, 1.0f, 0.0f }, { 0.0f, 0.0f, 1f, 1.0f } };
		float[][] Xiw = { { 0.70710677f, -0.40824828f, 0.57735026f, 0.0f }, { 0.0f, 0.8164966f, 0.57735026f, 0.0f },
				{ -0.70710677f, -0.40824828f, 0.57735026f, 0.0f }, { 0.0f, 0.0f, 0.0f, 1.0f } };
		for (int i = 0; i < verts.length; i++)
		{
			verts[i] = ComUtils.Multiply(Xiw, verts[i]);
//			verts[i] = ComUtils.Multiply(Xpi, verts[i]);
//			verts[i] = ComUtils.Multiply(Xsp, verts[i]);
//			verts[i] = ComUtils.Multiply(1 / verts[i][3][0], verts[i]);
			for (int j = 0; j < verts[i].length - 1; j++)
				System.out.print("" + verts[i][j][0] + "\t");
			System.out.println();
		}
	}
}
