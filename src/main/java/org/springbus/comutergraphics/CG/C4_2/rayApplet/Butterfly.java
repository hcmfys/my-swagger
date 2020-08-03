package org.springbus.comutergraphics.CG.C4_2.rayApplet;
// 本ファイルの著作権は、株式会社オーム社および本書の著作者である青野雅樹
// および日本アイビーエム（株）に帰属します。
// 本ファイルを利用したことによる直接あるいは間接的な損害に関して、
// 著作者およびオーム社はいっさいの責任を負いかねますので、
// あらかじめご了承ください
// また，本ファイルを他のウェブサイトで公開すること，およびCD-ROMなどの
// ディジタルメディアで再配布すること，ならびに販売目的で使用することは
// お断りします。

// Butterfly.java
//「 蝶」のシーングラフ
//	プログラム４−４

import org.springbus.comutergraphics.CG.common.*;

public class Butterfly extends MyObject {

	public ObjectNode butterfly;
	public ObjectNode leftWing;
	public ObjectNode rightWing;
	
	public Butterfly(JApplet applet, ObjectNode universe){

		Index3[] upLeftWingIndex = new Index3[1];
		upLeftWingIndex[0] = new Index3(0,2,1);
		Index3[] upRightWingIndex = new Index3[1];
		upRightWingIndex[0] = new Index3(0,1,2);
		Index3[] botLeftWingIndex = new Index3[2];
		botLeftWingIndex[0] = new Index3(0,1,3);
		botLeftWingIndex[1] = new Index3(1,2,3);
		Index3[] botRightWingIndex = new Index3[2];
		botRightWingIndex[0] = new Index3(0,3,1);
		botRightWingIndex[1] = new Index3(1,3,2);
		Vertex3[] upLeftWingVertex = new Vertex3[3];
		upLeftWingVertex[0] = new Vertex3(0,0,0);
		upLeftWingVertex[1] = new Vertex3(-10,0,0);
		upLeftWingVertex[2] = new Vertex3(-11,0,-10);
		Vertex3[] upRightWingVertex = new Vertex3[3];
		upRightWingVertex[0] = new Vertex3(0,0,0);
		upRightWingVertex[1] = new Vertex3(10,0,0);
		upRightWingVertex[2] = new Vertex3(11,0,-10);
		Vertex3[] botLeftWingVertex = new Vertex3[4];
		botLeftWingVertex[0] = new Vertex3(0,0,0);
		botLeftWingVertex[1] = new Vertex3(-10,0,1);
		botLeftWingVertex[2] = new Vertex3(-11,0,12);
		botLeftWingVertex[3] = new Vertex3(-5,0,8);
		Vertex3[] botRightWingVertex = new Vertex3[4];
		botRightWingVertex[0] = new Vertex3(0,0,0);
		botRightWingVertex[1] = new Vertex3(10,0,1);
		botRightWingVertex[2] = new Vertex3(11,0,12);
		botRightWingVertex[3] = new Vertex3(5,0,8);
		Vertex2[] upLeftWingTexCoord = new Vertex2[3];
		upLeftWingTexCoord[0] = new Vertex2(0.95,0.60);
		upLeftWingTexCoord[1] = new Vertex2(0.45,0.5);
		upLeftWingTexCoord[2] = new Vertex2(0.20,0.95);
		Vertex2[] upRightWingTexCoord = new Vertex2[3];
		upRightWingTexCoord[0] = new Vertex2(0.02,0.61);
		upRightWingTexCoord[1] = new Vertex2(0.66,0.60);
		upRightWingTexCoord[2] = new Vertex2(0.77,0.99);
		Vertex2[] botLeftWingTexCoord = new Vertex2[4];
		botLeftWingTexCoord[0] = new Vertex2(0.94,0.51);
		botLeftWingTexCoord[1] = new Vertex2(0.46,0.47);
		botLeftWingTexCoord[2] = new Vertex2(0.43,0.12);
		botLeftWingTexCoord[3] = new Vertex2(0.87,0.20);
		Vertex2[] botRightWingTexCoord = new Vertex2[4];
		botRightWingTexCoord[0] = new Vertex2(0.04,0.51);
		botRightWingTexCoord[1] = new Vertex2(0.54,0.51);
		botRightWingTexCoord[2] = new Vertex2(0.59,0.16);
		botRightWingTexCoord[3] = new Vertex2(0.08,0.23);
		
		TriangleSet upLeftWingTS = new
                TriangleSet(1,upLeftWingIndex,3,upLeftWingVertex,
				upLeftWingTexCoord);
		TriangleSet upRightWingTS = new
                TriangleSet(1,upRightWingIndex,3,upRightWingVertex,
				upRightWingTexCoord);
		TriangleSet botLeftWingTS = new
                TriangleSet(2,botLeftWingIndex,4,botLeftWingVertex,
				botLeftWingTexCoord);
		TriangleSet botRightWingTS = new
                TriangleSet(2,botRightWingIndex,4,botRightWingVertex,
				botRightWingTexCoord);
		Sphere antennaBallSphere = new Sphere(0.4);
		Sphere headBallSphere = new Sphere(2.0);
		Cylinder antennaCylinder = new Cylinder(0.2,8);
		Cylinder bodyCylinder = new Cylinder(1,10);
		Cone bodyCone = new Cone(1,2);

		Group butterflyGroup = new Group();
		Group headGroup = new Group();
		Group bodyGroup = new Group();
		Group bellyGroup = new Group();
		Group leftWingGroup = new Group();
		Group rightWingGroup = new Group();
		Group leftAntennaGroup = new Group();
		Group rightAntennaGroup = new Group();

		butterfly = universe.addChild(butterflyGroup,"蝶");
		ObjectNode head = butterfly.addChild(headGroup,"頭部");
		ObjectNode body = butterfly.addChild(bodyGroup,"胴体");
		ObjectNode leftAntenna = head.addChild(leftAntennaGroup,"左触角");
		ObjectNode rightAntenna = head.addChild(rightAntennaGroup,"右触角");
		ObjectNode belly = body.addChild(bellyGroup,"腹部");
		leftWing = body.addChild(leftWingGroup,"左翅");
		rightWing = body.addChild(rightWingGroup,"右翅");
		ObjectNode headBall = head.addChild(headBallSphere,"頭部球");
		ObjectNode leftAntennaCyl = 
			leftAntenna.addChild(antennaCylinder,"左触角円柱");
		ObjectNode leftAntennaBall = 
			leftAntenna.addChild(antennaBallSphere,"左触角球");
		ObjectNode rightAntennaCyl = 
			rightAntenna.addChild(antennaCylinder,"右触角円柱");
		ObjectNode rightAntennaBall = 
			rightAntenna.addChild(antennaBallSphere,"右触角球");
		ObjectNode bellyTop = belly.addChild(bodyCone,"腹部上部円錐");
		ObjectNode bellyBot = belly.addChild(bodyCone,"腹部下部円錐");
		ObjectNode bellyCenter = belly.addChild(bodyCylinder,"腹部円柱");
		ObjectNode upLeftWing = leftWing.addChild(upLeftWingTS,
			"左上部翅");
		ObjectNode botLeftWing = leftWing.addChild(botLeftWingTS,
			"左下部翅");
		ObjectNode upRightWing = rightWing.addChild(upRightWingTS,
			"右上部翅");
		ObjectNode botRightWing = rightWing.addChild(botRightWingTS,
			"右下部翅");
		//各種変換
		bellyTop.rotate(1,0,0,Math.PI/2);
		bellyTop.translate(0,0,-6);
		bellyCenter.rotate(1,0,0,Math.PI/2);
		bellyBot.rotate(1,0,0,-Math.PI/2);
		bellyBot.translate(0,0,6);
		leftAntennaCyl.rotate(1,0,0,Math.PI/2);
		leftAntennaBall.translate(0,0,-4.2);
		leftAntenna.rotate(0,1,0,-0.5);
		leftAntenna.translate(-1.5,0,-4);
		rightAntennaCyl.rotate(1,0,0,Math.PI/2);
		rightAntennaBall.translate(0,0,-4.2);
		rightAntenna.rotate(0,1,0,0.5);
		rightAntenna.translate(1.5,0,-4);
		leftWing.translate(-0.5,0,-2.5);
		rightWing.translate(0.5,0,-2.5);
		head.translate(0,0,-6);
		butterfly.translate(0,0,1);

		Material mat1 = new Material();
		mat1.setDiffuseColor(0.2,0.2,0.6);
		mat1.setSpecularColor(0.5,0.5,0.5);
		mat1.setShininess(200.0);
		belly.setMaterial(mat1);

		Material mat2 = new Material();
		mat2.setEmissiveColor(0.1,0.1,0.1);
		mat2.setDiffuseColor(1,0,0);
		mat2.setSpecularColor(1,1,1);
		mat2.setShininess(120.0);
		head.setMaterial(mat2);

		Material mat3 = new Material();
		mat3.setEmissiveColor(0.1,0.1,0.1);
		mat3.setDiffuseColor(1,1,0);
		mat3.setSpecularColor(1,1,1);
		mat3.setShininess(120.0);
		butterfly.setMaterial(mat3);
		
		Texture leftWingTexture = new Texture(applet,
			applet.getCodeBase(),"lw.jpg");
		leftWingTexture.loadImage();
		Material mat4 = new Material();
		mat4.setEmissiveColor(0.1,0.1,0.1);
		mat4.setDiffuseColor(1,1,1);
		mat4.setSpecularColor(0.5,0.5,0.5);
		mat4.setShininess(120.0);
		mat4.setTexture(leftWingTexture);
		leftWing.setMaterial(mat4);

		Texture rightWingTexture = new Texture(applet,
			applet.getCodeBase(),"rw.jpg");
		rightWingTexture.loadImage();
		Material mat5 = new Material();
		mat5.setEmissiveColor(0.1,0.1,0.1);
		mat5.setDiffuseColor(1,1,1);
		mat5.setSpecularColor(0.5,0.5,0.5);
		mat5.setShininess(120.0);
		mat5.setTexture(rightWingTexture);
		rightWing.setMaterial(mat5);

	}
}
