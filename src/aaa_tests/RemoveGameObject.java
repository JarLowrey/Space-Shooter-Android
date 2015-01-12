package aaa_tests;

import java.util.ArrayList;

import org.junit.Test;

import junit.framework.TestCase;

public class RemoveGameObject extends TestCase {

	public static void main(String[] args){
		test();
	}
	
	@Test
	public static void test(){
		ArrayList<String> a = new ArrayList<String>();
		String msg="aaa";
		a.add(msg);
		msg+="bbb";
		boolean remove = a.remove(msg);
		assertEquals(remove, true);
	}
}
