package myGL;

public class Camera
{
	Matrix Xiw = new Matrix(); // xform from world to image space
	Matrix Xpi = new Matrix(); // perspective projection xform
	Coord position = new Coord(); // position of image plane origin
	Coord lookat = new Coord(); // position of look-at-point
	Coord worldup = new Coord(); // world up-vector (almost screen up)
	float FOV; // horizontal field of view
}
