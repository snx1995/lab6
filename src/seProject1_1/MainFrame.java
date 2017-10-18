package seProject1_1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.chrono.JapaneseChronology;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.*;
import javax.swing.border.Border;

public class MainFrame {

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			JFrame jf = new JFrame("生成有向图");
			jf.setLocation(300, 150);
			jf.setLayout(new GridBagLayout());

			GridBagConstraints gbc = new GridBagConstraints();
			jf.getContentPane().setBackground(Color.white);
			jf.setSize(400, 150);
			jf.setResizable(false);
			jf.setDefaultCloseOperation(3);

			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.weightx = 0;
			gbc.weighty = 0;
			gbc.anchor = GridBagConstraints.CENTER;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.insets = new Insets(5, 5, 5, 5);
			gbc.ipadx = 1;
			gbc.ipady = 1;
			JTextArea jArea = new JTextArea();
			jArea.setText("Please input file path:");
			jArea.setEditable(false);
			Font font = new Font("Microsoft Ya Hei", 1, 16);
			jArea.setFont(font);
			jf.add(jArea, gbc);

			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.gridwidth = 4;
			gbc.gridheight = 1;
			gbc.weightx = 4;
			gbc.weighty = 2;
			gbc.anchor = GridBagConstraints.NORTH;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.insets = new Insets(5, 5, 5, 5);
			gbc.ipadx = 1;
			gbc.ipady = 1;
			JTextField jtf = new JTextField();
			Font jtfFont = new Font("Microsoft Ya Hei", 0, 16);
			jtf.setFont(jtfFont);
			jf.add(jtf, gbc);

			gbc.gridx = 3;
			gbc.gridy = 2;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.weightx = 1;
			gbc.weighty = 1;
			gbc.anchor = GridBagConstraints.NORTH;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.insets = new Insets(5, 0, 5, 5);
			gbc.ipadx = 1;
			gbc.ipady = 1;
			JButton jButton = new JButton("Confirm");
			jf.add(jButton, gbc);

			gbc.gridx = 2;
			gbc.gridy = 2;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.weightx = 1;
			gbc.weighty = 1;
			gbc.anchor = GridBagConstraints.NORTH;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.insets = new Insets(5, 0, 5, 5);
			gbc.ipadx = 1;
			gbc.ipady = 1;
			JButton jButton2 = new JButton("Browse");
			jf.add(jButton2, gbc);

			jf.setVisible(true);

			JFrame jf2 = new JFrame("ShowImage");
			jf2.setDefaultCloseOperation(3);

			jButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					//System.out.println(jtf.getText());
					String filePath = jtf.getText();

					// if(filePath.matches(""))
					Graph graph = Project1_1.createDirectedGraph(filePath);
					Project1_1.showDirectedGraph(graph);
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					}
					
					new MainFrame(graph);
					jf.dispose();
				}
			});

			JFileChooser jFileChooser = new JFileChooser();
			File dir = new File("C:\\Users\\Snxt\\Desktop");
			jFileChooser.setCurrentDirectory(dir);
			jButton2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int result = jFileChooser.showOpenDialog(null);
					if (result == jFileChooser.APPROVE_OPTION) {
						jtf.setText(jFileChooser.getSelectedFile().toString());
					} else {
						System.out.println("No file selected!");
					}
				}
			});
		});
	}

	public MainFrame(Graph G) {
		EventQueue.invokeLater(() -> {
			JFrame frame = new JFrame();
			frame.setTitle("实验1");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(950, 1000);
			frame.setResizable(false);
			Image image = new ImageIcon("icons\\testIcon2.png").getImage();
			frame.setIconImage(image);

			frame.setLayout(new BorderLayout());
			ShowImageComponent sic = new ShowImageComponent(G);
			Border imgBorder = BorderFactory.createEtchedBorder();
			Border imgTitledBorder = BorderFactory.createTitledBorder(imgBorder, "Directed Graph");
			sic.setBorder(imgTitledBorder);

			JScrollPane jScrollPane = new JScrollPane(sic);
			Border btnTitled = BorderFactory.createTitledBorder(imgBorder, "Options");
			Border resultTitled = BorderFactory.createTitledBorder(imgBorder, "Result");

			ResultPanel result = new ResultPanel();
			result.setBorder(resultTitled);

			ButtonPanel buttonsPart = new ButtonPanel(result, sic, G);
			buttonsPart.setBorder(btnTitled);

			frame.add(result, BorderLayout.SOUTH);
			frame.add(sic, BorderLayout.CENTER);
			frame.add(buttonsPart, BorderLayout.EAST);
			frame.setVisible(true);
			sic.repaint();
		});
	}

}

class ButtonPanel extends JPanel {
	private JButton bt1;
	private JButton bt2;
	private JButton bt3;
	private JButton bt4;
	private JButton bt5;
	private ResultPanel result;
	private ShowImageComponent showImg;
	private boolean pause;

	private int flag = -1;

	private StringMessage message;

	private String[] randomPath;
	
	public ButtonPanel(ResultPanel r, ShowImageComponent s, Graph G) {
		bt5 = new JButton("停止游走");
		bt4 = new JButton("随机游走");
		bt3 = new JButton("最短路径");
		bt2 = new JButton("生成新文本");
		bt1 = new JButton("桥接词");
		bt5.setVisible(false);
		result = r;
		showImg = s;
		message = new StringMessage();
		setLayout(new GridLayout(18, 1));

		bt1.addActionListener(event -> bridgeWordsFrame(G));
		bt2.addActionListener(event -> newTextFrame(G));
		bt3.addActionListener(event -> shortestWayFrame(G));
		bt4.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				flag++;
				if(flag==0) {
					randomPath = Project1_1.randomWalk(G).split("[^a-z]+");
					result.setResult(randomPath[0]);
				}
				else {
					String path = randomPath[0];
					for(int i=1;i<=flag;i++) {
						path += " " +randomPath[i];
					}
					result.setResult(path);
				}
				if(flag>=randomPath.length-1) {
					bt4.setText("随机游走");
					flag=-1;
				}
				else bt4.setText("继续游走");
			}
		});
		add(bt5, 8, 0);
		add(bt4, 6, 0);
		add(bt3, 4, 0);
		add(bt2, 2, 0);
		add(bt1, 0, 0);

	}

	public void bridgeWordsFrame(Graph G) {
		EventQueue.invokeLater(() -> {
			JFrame jFrame = new JFrame("请输入两个单词");
			jFrame.setLayout(new GridLayout(3, 2));
			jFrame.setDefaultCloseOperation(2);
			jFrame.setSize(300, 150);
			jFrame.setResizable(false);
			jFrame.setLocation(600, 200);
			Font font = new Font("Microsoft YaHei", 1, 12);
			JLabel label1 = new JLabel("Word1:");
			JLabel label2 = new JLabel("word2:");
			label1.setFont(font);
			label2.setFont(font);

			JTextField textField1 = new JTextField();
			JTextField textField2 = new JTextField();

			JButton bridgeCancel = new JButton("取消");
			JButton bridgeConfirm = new JButton("确认");

			jFrame.add(bridgeCancel, 2, 0);
			jFrame.add(bridgeConfirm, 2, 1);
			jFrame.add(label2, 1, 0);
			jFrame.add(textField2, 1, 1);
			jFrame.add(label1, 0, 0);
			jFrame.add(textField1, 0, 1);

			bridgeCancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					result.setResult("Operation canceled!");
					jFrame.dispose();
				}
			});
			bridgeConfirm.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					message.msg1 = textField1.getText();
					message.msg2 = textField2.getText();
					result.setResult(Project1_1.queryBridgeWords(G, message.msg1, message.msg2));
					jFrame.dispose();
				}
			});
			jFrame.setVisible(true);
		});
	}

	public void newTextFrame(Graph G) {
		EventQueue.invokeLater(() -> {
			Font font = new Font("Microsoft YaHei", 1, 14);
			JFrame newTextFrame = new JFrame("生成新文本");
			newTextFrame.setLayout(new BorderLayout());
			newTextFrame.setSize(670, 230);
			newTextFrame.setDefaultCloseOperation(2);
			newTextFrame.setResizable(false);
			newTextFrame.setLocation(600, 200);
			JPanel p1 = new JPanel();
			JPanel p2 = new JPanel();
			JPanel p3 = new JPanel();

			Border b1 = BorderFactory.createEtchedBorder();

			p2.setBorder(b1);
			p3.setBorder(b1);

			p3.setLayout(new GridLayout(5, 1));

			JTextField mesg = new JTextField();
			mesg.setText("请输入原文本:");
			mesg.setFont(font);
			mesg.setEditable(false);

			JTextArea originalText = new JTextArea(8, 40);
			originalText.setFont(font);
			originalText.setLineWrap(true);

			JButton newTextCancel = new JButton("取消");
			JButton newTextConfirm = new JButton("确认");

			p1.add(mesg);
			p2.add(originalText);
			p3.add(newTextCancel, 1, 0);
			p3.add(newTextConfirm, 0, 0);

			newTextCancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					result.setResult("Operation canceled!");
					newTextFrame.dispose();
				}
			});
			newTextConfirm.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					message.msg1 = originalText.getText();
					result.setResult("New text : " + "\n" + Project1_1.generateNewText(G, message.msg1));
					newTextFrame.dispose();
				}
			});

			newTextFrame.add(p1, BorderLayout.NORTH);
			newTextFrame.add(p2, BorderLayout.CENTER);
			newTextFrame.add(p3, BorderLayout.EAST);
			newTextFrame.setVisible(true);

		});
	}

	public void shortestWayFrame(Graph G) {
		EventQueue.invokeLater(() -> {
			JFrame jFrame = new JFrame("计算最短路径");
			jFrame.setSize(300, 150);
			jFrame.setLayout(new GridLayout(3, 2));
			jFrame.setResizable(false);
			jFrame.setDefaultCloseOperation(2);

			JButton shortestWayCancel = new JButton("取消");
			JButton shortestWayConfirm = new JButton("确认");
			JLabel word1 = new JLabel("Word1 : ");
			JLabel word2 = new JLabel("Word2 : ");
			JTextField t1 = new JTextField();
			JTextField t2 = new JTextField();

			shortestWayCancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					result.setResult("Operation canceled!");
					jFrame.dispose();
				}
			});
			shortestWayConfirm.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					message.msg2 = t1.getText().toLowerCase();
					message.msg1 = t2.getText().toLowerCase();
					String r = Project1_1.calcShortestPath(G, message.msg1, message.msg2);
					result.setResult(r);
					if(!r.equals("word1 or word2 not in graph")) showImg.setImgFilePath("dotGif.gif");
					//showImg.repaint();
					jFrame.dispose();
				}
			});

			jFrame.add(shortestWayCancel, 2, 0);
			jFrame.add(shortestWayConfirm, 2, 1);
			jFrame.add(word1, 1, 0);
			jFrame.add(t1, 1, 1);
			jFrame.add(word2, 0, 0);
			jFrame.add(t2, 0, 1);

			jFrame.setVisible(true);
		});
	}

	public void randomWalkFrame(String[] randomPath, int i) {

	}
}

class ResultPanel extends JPanel {
	private JTextArea resultText;
	private String result;
	private JLabel label;

	public ResultPanel() {
		resultText = new JTextArea("Everything ready!" + '\n' + "Please choose an option.", 4, 57);
		resultText.setLineWrap(true);
		Font textFont = new Font("Microsoft YaHei", 1, 16);
		resultText.setFont(textFont);
		resultText.setEditable(false);
		label = new JLabel("Result : ");
		// add(label);
		add(resultText);
	}

	public void setResult(String result) {
		resultText.setText(result);
		this.result = result;
	}
	
}

class ShowImageComponent extends JComponent {
	public static final int DISPLAY_X = 0;
	public static final int DISPLAY_Y = 50;

	private static final int DEFAULT_WIDTH = 800;
	private static final int DEFAULT_HEIGHT = 800;

	private Image image;
	private String imageFilePath;

	private JLabel picLabel;

	public ShowImageComponent(Graph G) {
		//Project1_1.showDirectedGraph(G);
		image = getToolkit().getImage("dotGif.gif");
		imageFilePath = "dotGif.gif";
		Icon pic = new ImageIcon("dotGif.gif");
		File imgfile = new File("dotGif.gif");
		setLayout(new BorderLayout());
		picLabel = new JLabel("");
		picLabel.setIcon(pic);
		// add(picLabel,BorderLayout.CENTER);
		// try {
		// picLabel.setIcon(new ImageIcon(ImageIO.read(imgfile)));
		// } catch (IOException e) {
		// // TODO 自动生成的 catch 块
		// e.printStackTrace();
		// }
		repaint();
	}
	public void repaintImg() {
		image = new ImageIcon(imageFilePath).getImage();
		repaint();
	}
	
	public void paintComponent(Graphics G) {
		super.paintComponents(G);
		System.out.println("Show Image");
		// Image img = getToolkit().getImage(imageFilePath);
		// Image img = new ImageIcon(imageFilePath).getImage();
		// Image img2 = getToolkit().getImage(imageFilePath);
		// G.drawImage(img2, (DEFAULT_WIDTH - img2.getWidth(this)) / 2, (DEFAULT_HEIGHT
		// - img2.getHeight(this)) / 2, this);
		// G.dispose();
		// img2 = getToolkit().getImage(imageFilePath);
		// G.drawImage(image, (DEFAULT_WIDTH - image.getWidth(this)) / 2,
		// (DEFAULT_HEIGHT - image.getHeight(this)) / 2, DEFAULT_WIDTH ,DEFAULT_HEIGHT,
		// null);
		G.drawImage(image, 30, 30, DEFAULT_WIDTH - 10, DEFAULT_HEIGHT - 10, null);
	}

	public void setImgFilePath(String filepath) {
		image.flush();
		imageFilePath = filepath;
		image = new ImageIcon(imageFilePath).getImage();
		repaint();
	}
	
	public void paint(Graphics G) {
		image = new ImageIcon(imageFilePath).getImage();
		G.drawImage(image, 30, 30, DEFAULT_WIDTH - 10, DEFAULT_HEIGHT - 10, null);
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
}

class StringMessage {
	public String msg1 = "";
	public String msg2 = "";
}
