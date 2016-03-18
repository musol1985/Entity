package com.entity.test.network;

import com.entity.network.core.msg.sync.NetMessage;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public  class NetPosition extends NetMessage<TestNetCharacter>{

	public Vector3f pos=new Vector3f();
	public float ang=0;
	
	@Override
	public void onSend(TestNetCharacter model) {
		pos=model.getWorldTranslation();
		ang=model.getWorldRotation().getW();
	}
	@Override
	public void onReceive(TestNetCharacter model) {
		model.setLocalTranslation(pos);
		model.setLocalRotation(new Quaternion().fromAngles(0, ang, 0));
	}
	

}
