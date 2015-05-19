import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class SortPhonebook {
	private static ArrayList<PhoneEntry> _phonebook;
	private static JPanel _buttonholder;
	private static JPanel _labelholder;
		
	public static void main(String[] args) {
		JFrame frame = new JFrame("Sort Comparison");
		frame.setLayout(new BorderLayout());
		frame.setPreferredSize(new Dimension(300,200));
		readFile();

		_buttonholder = new JPanel();
		_buttonholder.setLayout(new GridLayout(2,0));
		frame.add(_buttonholder, BorderLayout.WEST);
		
		_labelholder = new JPanel();
		_labelholder.setLayout(new GridLayout(2,0));
		frame.add(_labelholder, BorderLayout.EAST);
		
		// labels are final so they may be modified by anonymous functions
		final JLabel seleclabel = new JLabel();
		_labelholder.add(seleclabel);
		final JLabel mergelabel = new JLabel();
		_labelholder.add(mergelabel);
		
		JButton selecbutton = new JButton("Selection sort");
		selecbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				// makes a new thread which runs and times selection sort
				Thread selecthread = new Thread() {
					public void run() {
						double starttime = System.nanoTime();
						ArrayList<PhoneEntry> selsort = selectionSort(_phonebook);
						double totaltime = System.nanoTime()-starttime;
						seleclabel.setText(String.valueOf(totaltime/1000000000));
						
						if (!isSorted(selsort)) {
							System.err.println("Selection sort doesn't sort!");
						}
					}
				};
				selecthread.start();
			}
		});
		_buttonholder.add(selecbutton);
		
		JButton mergebutton = new JButton("Merge sort");
		mergebutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ev) {
				// makes a new thread which runs and times merge sort
				Thread mergethread = new Thread() {
					public void run() {
						double starttime = System.nanoTime();
						ArrayList<PhoneEntry> mersort = mergeSort(_phonebook);
						double totaltime = System.nanoTime()-starttime;
						mergelabel.setText(String.valueOf(totaltime/1000000000));
						
						if (!isSorted(mersort)) {
							System.err.println("Merge sort doesn't sort!");
						}						
					}
				};
				mergethread.start();
			}
		});		
		_buttonholder.add(mergebutton);
		
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	/* reads the phonebook file into an arraylist */
	private static void readFile() {
		_phonebook = new ArrayList<PhoneEntry>();
		try {
			FileReader f = new FileReader("phonebook.txt");
			BufferedReader br = new BufferedReader(f);
			String line = br.readLine();
			while (line!=null) {
				String[] parts = line.split(" ");
				PhoneEntry newentry = new PhoneEntry(parts[1]+parts[2],Integer.parseInt(parts[0]));
				_phonebook.add(newentry);
				line = br.readLine();
			}
		} catch (Exception e) {
			System.err.print("File Reading Error!");
			e.printStackTrace();
		}
	}
	
	/* Implementation of selection sort derived from Wikipedia */
	private static ArrayList<PhoneEntry> selectionSort(ArrayList<PhoneEntry> phonebook) {
		ArrayList<PhoneEntry> selectionsorted = new ArrayList<PhoneEntry>(phonebook);
		PhoneEntry currfirst; int currmin;
		for (int i=0; i<selectionsorted.size(); i++) {
			currfirst = selectionsorted.get(i);
			currmin = i;
			for (int j=i; j<selectionsorted.size(); j++) {
				if (currfirst.getName().compareTo(selectionsorted.get(j).getName()) > 0) {
					currfirst = selectionsorted.get(j); 
					currmin = j;
				}
			}
			selectionsorted.set(currmin,selectionsorted.get(i));
			selectionsorted.set(i, currfirst);
		}
		return selectionsorted;
	}
	
	/* Implementation of merge sort derived from Wikipedia */
	private static ArrayList<PhoneEntry> mergeSort(ArrayList<PhoneEntry> phonebook) {
		if (phonebook.size() <= 1) {
			return phonebook;
		}
		ArrayList<PhoneEntry> leftbook = new ArrayList<PhoneEntry>(phonebook.subList(0,phonebook.size()/2));
		ArrayList<PhoneEntry> rightbook = new ArrayList<PhoneEntry>(phonebook.subList(phonebook.size()/2, phonebook.size()));
		leftbook = mergeSort(leftbook);
		rightbook = mergeSort(rightbook);
		return merge(leftbook,rightbook);
	}
	
	/* merge function for use in mergesort */
	private static ArrayList<PhoneEntry> merge(ArrayList<PhoneEntry> phonebook1, ArrayList<PhoneEntry> phonebook2) {
		ArrayList<PhoneEntry> mergedbook = new ArrayList<PhoneEntry>(phonebook1.size() + phonebook2.size());

		while (phonebook1.size() > 0 || phonebook2.size() > 0) {
			if (phonebook1.size() > 0 && phonebook2.size() > 0) {
				PhoneEntry curr1 = phonebook1.get(0);
				PhoneEntry curr2 = phonebook2.get(0);
				if (curr1.getName().compareTo(curr2.getName()) <= 0) {
					mergedbook.add(curr1);
					phonebook1.remove(0);
				} else {
					mergedbook.add(curr2);
					phonebook2.remove(0);
				}
			} else if (phonebook1.size() > 0) {
				mergedbook.addAll(phonebook1);
				phonebook1.clear();
			} else {
				mergedbook.addAll(phonebook2);
				phonebook2.clear();
			}
		}
		return mergedbook;
	}
	
	/* Checks an ArrayList for sortedness*/
	private static boolean isSorted(ArrayList<PhoneEntry> entrylist) {
		PhoneEntry last = entrylist.get(0); PhoneEntry curr;
		for (int i=0;i<entrylist.size();i++) {
			curr = entrylist.get(i);
			if (curr.getName().compareTo(last.getName()) < 0) {
				return false;
			}
		}
		return true;
	}

}
