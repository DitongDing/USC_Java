package test;

import Luxand.*;
import Luxand.FSDK.*;
import Luxand.FSDKCam.*;
import utils.ComUtils;
import utils.Constants;

public class TestExtractFacialLocation {
	public static String fileName = "input/jaffe/KA.AN1.39.jpg";

	public static void main(String[] args) throws Exception {
		ComUtils.extractFacialLocation_ByFolder("input/jaffe", "output/jaffe");
	}
}
