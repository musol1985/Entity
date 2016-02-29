package com.entity.network.core.beans;

import java.util.ArrayList;
import java.util.List;


public class CellViewQuad {
	/**
	 * |---|---|
	 * | 2 | 3 |
	 * |---|---|
	 * | 1 | 0 |
	 * |---|---|
	 * 
	 */
	public CellId[] cells=new CellId[4];

	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CellViewQuad){
			CellViewQuad v=(CellViewQuad)obj;
			for(int i=0;i<cells.length;i++){
				if(!sameCell(getCellById(i),v.getCellById(i))){
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean hasCell(CellId c){
		if(c!=null)
			for(CellId myCell:cells){
				if(myCell!=null && myCell.equals(c))
					return true;
			}
		return false;
	}
	
	private boolean sameCell(CellId c1, CellId c2){
		return (c1!=null && c2!=null && c1.equals(c2));
	}
	public CellId getCellById(int id){
		return cells[id];
	}
	
	public CellId getCell0(){
		return cells[0];
	}
	public CellId getCell1(){
		return cells[1];
	}
	public CellId getCell2(){
		return cells[2];
	}
	public CellId getCell3(){
		return cells[3];
	}
	public void setCell0(CellId c){
		cells[0]=c;
	}
	public void setCell1(CellId c){
		cells[1]=c;
	}
	public void setCell2(CellId c){
		cells[2]=c;
	}
	public void setCell3(CellId c){
		cells[3]=c;
	}
	
	
	public String toString(){
		return super.toString()+" \n	c0->"+cell2String(0)+"\n	c1->"+cell2String(1)+"\n	c2->"+cell2String(2)+"\n	c3->"+cell2String(3);
	}
	
	private String cell2String(int index){
		CellId c=cells[index];
		if(c!=null)
			return c.toString();
		return "null";
	}
	
	/**
	 * Get cells that aren't in view
	 * @param view1
	 * @param view2
	 * @return
	 */
	public List<CellId> getCellsNotIn(CellViewQuad view){
		List<CellId> res=new ArrayList<CellId>(4);
		
		for(CellId c:cells){
			if(!view.hasCell(c))
				res.add(c);
		}
		
		return res;
	}
}
