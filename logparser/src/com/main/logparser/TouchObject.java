package com.main.logparser;

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
	private float X;
	private float Y;
	private int finger;
	private int dir;
	
	public float getX() {
		return X;
	}
	public void setX(int x) {
		X = x;
	}
	public float getY() {
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
	public int isDir() {
		return dir;
	}
	public void setDir(int dir) {
		this.dir = dir;
	}
	public TimeStampObject getT() {
		return t;
	}
	public void setT(TimeStampObject T) {
		this.t= T;
	}
	
	
	public TouchObject(TouchObject t){
		super();
	}

	public TouchObject(TimeStampObject T, int x, int y, int finger, int dir) {
		super();
		this.t = T;
		X = x;
		Y = y;
		this.finger = finger;
		this.dir = dir;
	}
	
	public static String display(TouchObject T){
		if(T.dir==0){
			return (T.t.mmdd+" "+T.t.time+" "+T.X+" "+T.Y+" "+T.finger+" PRESS EVENT ").toString();
		}
		else if(T.dir==1){
			return (T.t.mmdd+" "+T.t.time+" "+T.X+" "+T.Y+" "+T.finger+" MOVE EVENT ").toString();
		}
		else{
			return (T.t.mmdd+" "+T.t.time+" "+T.X+" "+T.Y+" "+T.finger+" RELEASE EVENT ").toString();
		}
	}
	public static void displayObject(TouchObject T){
		if(T.dir==0){
			System.out.println(T.t.mmdd+" "+T.t.time+" "+T.X+" "+T.Y+" "+T.finger+" PRESS EVENT ");
			}
		else if(T.dir==1){
			System.out.println(T.t.mmdd+" "+T.t.time+" "+T.X+" "+T.Y+" "+T.finger+" MOVE EVENT ");
		}
		else{
			System.out.println(T.t.mmdd+" "+T.t.time+" "+T.X+" "+T.Y+" "+T.finger+" RELEASE EVENT");
		}
	}
	
}
