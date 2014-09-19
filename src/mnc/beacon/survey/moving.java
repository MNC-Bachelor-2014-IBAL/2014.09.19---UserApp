package mnc.beacon.survey;

import java.util.*;

public class moving {

	// Queue<Integer> Myque = new Queue<Integer>(); // Queue Ŭ���� �ν��Ͻ���
	// LinkedList���� ��.
	// vals = new Queue<Integer>();
	double totalvalue;
	Queue<Double> queue = new LinkedList<Double>();

	double total(double rssi) { // ���� �ְ� �տ� delete

		if (queue.size() == 7) { // replace 7 with i.
			totalvalue -= queue.remove();
		}
		queue.add(rssi);
		totalvalue += rssi;

		if (queue.size() == 7) {
			return totalvalue / 7;

		}

		return 0;
	}

}
