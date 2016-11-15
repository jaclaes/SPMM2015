package org.cheetahplatform.modeler.engine.tdm;

import org.cheetahplatform.common.logging.PromLogger;
import org.cheetahplatform.modeler.engine.TDMModelingActivity;
import org.cheetahplatform.tdm.engine.TDMProcess;
import org.cheetahplatform.tdm.engine.TDMTest;

public abstract class AbstractChangeTestInitializer extends AbstractTDMInitializer {

	static int count = 1;

	public static void main(String[] args) {
		print(TDMModelingActivity.MODEL_11);
		print(TDMModelingActivity.MODEL_21);
		print(TDMModelingActivity.MODEL_31);

		print(TDMModelingActivity.MODEL_12);
		print(TDMModelingActivity.MODEL_22);
		print(TDMModelingActivity.MODEL_32);

		print(TDMModelingActivity.MODEL_13);
		print(TDMModelingActivity.MODEL_23);
		print(TDMModelingActivity.MODEL_33);

		print(TDMModelingActivity.MODEL_14);
		print(TDMModelingActivity.MODEL_24);
		print(TDMModelingActivity.MODEL_34);

		print(TDMModelingActivity.MODEL_15);
		print(TDMModelingActivity.MODEL_25);
		print(TDMModelingActivity.MODEL_35);
	}

	private static void print(long model) {
		System.out.println(count++);
		TDMProcess process = TDMModelingActivity.getProcess(model, new PromLogger());
		for (TDMTest test : process.getTests()) {
			System.out.println(test.getName().substring(3));
		}
		System.out.println("\n");

		TDMModelingActivity.cleanUpTDM();
	}

	protected char testCount;
	protected char a;
	protected char b;
	protected char c;
	protected char d;
	protected char e;
	protected char f;
	protected char g;
	protected char h;
	protected char i;
	protected char j;
	protected char k;
	protected char l;
	protected char m;
	protected char n;
	protected char o;
	protected char p;

	protected AbstractChangeTestInitializer(TDMProcess process) {
		super(process);

		testCount = 'a';
	}

	protected void initializeX1Mapping() {
		a = 'A';
		b = 'B';
		c = 'C';
		d = 'D';
		e = 'E';
		f = 'F';
		g = 'G';
		h = 'H';
		i = 'I';
		j = 'J';
		k = 'K';
		l = 'L';
		m = 'M';
		n = 'N';
		o = 'O';
		p = 'P';
	}

	protected void initializeX2Mapping() {
		a = 'Z';
		b = 'Y';
		c = 'X';
		d = 'W';
		e = 'V';
		f = 'U';
		g = 'T';
		h = 'S';
		i = 'R';
		j = 'Q';
		k = 'A';
		l = 'B';
		m = 'C';
		n = 'D';
		o = 'E';
		p = 'F';
	}

	protected void initializeX3Mapping() {
		a = 'E';
		b = 'F';
		c = 'G';
		d = 'H';
		e = 'I';
		f = 'J';
		g = 'K';
		h = 'L';
		i = 'M';
		j = 'N';
		k = 'O';
		l = 'P';
		m = 'Q';
		n = 'R';
		o = 'S';
		p = 'T';
	}

	protected void initializeX4Mapping() {
		a = 'H';
		b = 'I';
		c = 'J';
		d = 'K';
		e = 'L';
		f = 'M';
		g = 'N';
		h = 'O';
		i = 'P';
		j = 'Q';
		k = 'R';
		l = 'S';
		m = 'T';
		n = 'U';
		o = 'V';
		p = 'W';
	}

	protected void initializeX5Mapping() {
		a = 'J';
		b = 'K';
		c = 'L';
		d = 'M';
		e = 'N';
		f = 'O';
		g = 'P';
		h = 'Q';
		i = 'R';
		j = 'S';
		k = 'T';
		l = 'U';
		m = 'V';
		n = 'W';
		o = 'X';
		p = 'Y';
	}

}
