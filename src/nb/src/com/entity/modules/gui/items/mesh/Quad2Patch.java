package com.entity.modules.gui.items.mesh;

import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;

public class Quad2Patch extends Mesh{
	private float offset;
	private float width;
	private float height;
	
	public Quad2Patch(float offset) {		
		createGeometry(1, 1, offset);
	}
	

	public float getHeight() {
		return this.height;
	}

	public float getWidth() {
		return this.width;
	}
	
	public void updateSize(float width, float height){
		setBuffer(VertexBuffer.Type.Position, 3, new float[] {
				0.0f, 0.0f, 0.0f, offset, 0.0f, 0.0f, offset, height, 0.0f, 0.0f, height, 0.0f,
				offset, 0.0F, 0.0F, width-offset, 0.0F, 0.0F, width-offset, height, 0.0F, offset, height, 0.0F,
				width-offset, 0.0F, 0.0F, width, 0.0F, 0.0F, width, height, 0.0F, width-offset, height, 0.0F
		});
		
		updateBound();
		setStatic();
	}

	
	public void createGeometry(float width, float height, float offset) {
		this.offset=offset;
		this.width = width;
		this.height = height;
		
		setBuffer(VertexBuffer.Type.Position, 3, new float[] {
				0.0f, 0.0f, 0.0f, offset, 0.0f, 0.0f, offset, height, 0.0f, 0.0f, height, 0.0f,
				offset, 0.0F, 0.0F, width-offset, 0.0F, 0.0F, width-offset, height, 0.0F, offset, height, 0.0F,
				width-offset, 0.0F, 0.0F, width, 0.0F, 0.0F, width, height, 0.0F, width-offset, height, 0.0F
		});

		float txOffset=100-width/100;
		
		setBuffer(VertexBuffer.Type.TexCoord, 2, new float[] { 
				0.0F, 0.0F,txOffset, 0.0F, txOffset, 1.0F, 0.0F, 1.0F,
				txOffset, 0.0F,1.0F-txOffset, 0.0F, 1.0F-txOffset, 1.0F, txOffset, 1.0F,
				1.0F-txOffset, 0.0F,1.0F, 0.0F, 1.0F, 1.0F, 1.0F-txOffset, 1.0F
		});
		

		setBuffer(VertexBuffer.Type.Normal, 3, new float[] { 
				0.0F, 0.0F, 1.0F,0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 1.0F,
				0.0F, 0.0F, 1.0F,0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 1.0F,
				0.0F, 0.0F, 1.0F,0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 1.0F
		});


		setBuffer(VertexBuffer.Type.Index, 3, new short[] { 
				0, 1, 2, 0, 2, 3,
				4, 5, 6, 4, 6, 7,
				8, 9, 10, 8, 10, 11
		});
		

		updateBound();
		setStatic();
	}
}
