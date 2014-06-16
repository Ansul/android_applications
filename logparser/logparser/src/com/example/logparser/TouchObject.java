package com.example.logparser;

public class TouchObject{
	
	public static class TimeStampObject {
		public int mmdd;
		public float time;
		public TimeStampObject(int mmdd, float f) {
			//super();
			this.mmdd = mmdd;
			this.time = f;
		}
	}
	
	public TimeStampObject t;
	private int X;
	private int Y;
	private int finger;
	private boolean dir;
	private int pressure;
	
	public int getX() {
		return X;
	}
	public void setX(int x) {
		X = x;
	}
	public int getY() {
		return Y;
	}
	public void setY(int y) {
		Y = y;
	}
	public int getFinger() {
		return finger;
	}
	public void setFinger(int finger) {
		this.finger = finger;
	}
	public boolean isDir() {
		return dir;
	}
	public void setDir(boolean dir) {
		this.dir = dir;
	}
	public TimeStampObject getT() {
		return t;
	}
	public void setT(TimeStampObject T) {
		this.t= T;
	}
	public int getPressure() {
		return pressure;
	}
	public void setPressure(int pressure) {
		this.pressure = pressure;
	}
	
	public TouchObject(TouchObject t){
		super();
	}
	public TouchObject(TimeStampObject T, int x, int y, int finger, boolean dir, int pressure) {
		super();
		this.t = T;
		X = x;
		Y = y;
		this.finger = finger;
		this.dir = dir;
		this.pressure = pressure;
	}
	public TouchObject(TimeStampObject T,int x, int y, int finger, boolean dir) {
		super();
		this.t=T;
		X = x;
		Y = y;
		this.finger = finger;
		this.dir = dir;
	}
	public static String display(TouchObject T){
		if(T.dir){
			//System.out.println(T.t.mmdd+" "+T.t.time+" "+T.X+" "+T.Y+" "+T.finger+" PRESS EVENT "+T.pressure);
			return (T.t.mmdd+" "+T.t.time+" "+T.X+" "+T.Y+" "+T.finger+" PRESS EVENT "+T.pressure).toString();
		}
		else{
			//System.out.println(T.t.mmdd+" "+T.t.time+" "+T.X+" "+T.Y+" "+T.finger+" RELEASE EVENT");
			return (T.t.mmdd+" "+T.t.time+" "+T.X+" "+T.Y+" "+T.finger+" RELEASE EVENT ").toString();
		}
	}
	
}
