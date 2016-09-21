package ru.burningGlobe.generator;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

/**
 ** Created by espacio on 19.09.2016.
 **/

class MainWindow extends JFrame {
    private static final int WINDOW_X = 800; // actually, 805
    private static final int WINDOW_Y = 600; // actually, 687
    Font btnFont = new Font("Times New Roman", Font.PLAIN, 16);
    private File file;
    private static boolean fileSaved = true;

    private Map jpMap;

    MainWindow() {
        JMenuBar jmbMenu;
        JMenu jmFile, jmBrush, jmView;
        JMenuItem jmiCreate, jmiOpen, jmiSave, jmiExit;
        JMenuItem jmiBrushRoad, jmiBrushDesert,  jmiBrushStone, jmiBrushRb, jmiBrushNexus, jmiBrushEraser;

        setSize(WINDOW_X, WINDOW_Y);
        setResizable(false);
        setTitle("TD Maps Generator");
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        jmBrush = new JMenu("Кисть");
        jmView = new JMenu("Вид");

        jmbMenu = new JMenuBar();
        add(jmbMenu, BorderLayout.NORTH);
        jmFile = new JMenu("Файл");
        jmbMenu.add(jmFile);

        jmiSave = new JMenuItem("Сохранить");
        jmiSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        jmiSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (saveDialog())
                    saveFile();
            }
        });

        jmiCreate = new JMenuItem("Создать новую карту");
        jmiCreate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
        jmiCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!fileSaved)
                    saveOnCloseDialog();
                String s;
                int x, y;
                while (true) {
                    s = (String) JOptionPane.showInputDialog(
                            MainWindow.this,
                            "Введите размер карты (минимум: 15*15):",
                            "15*15");
                    if (s == null) break;
                    if (s.isEmpty()) continue;
                    if (!s.contains("*")) continue;
                    x = Integer.parseInt(s.substring(0, s.indexOf("*")));
                    if (x < 15) continue;
                    y = Integer.parseInt(s.substring(s.indexOf("*") + 1));
                    if (y < 15) continue;
                    jpMap = new Map(x, y);
                    add(jpMap, BorderLayout.CENTER);
                    jpMap.createNewMap();
                    fileSaved = false;
                    jmBrush.setEnabled(true);
                    jmView.setEnabled(true);
                    revalidate();
                    break;
                }
            }
        });
        jmFile.add(jmiCreate);

        jmiOpen = new JMenuItem("Открыть существующую карту");
        jmiOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        jmiOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!fileSaved)
                    saveOnCloseDialog();
                openFile();
                if (file != null && !fileSaved) {
                    jmBrush.setEnabled(true);
                    jmView.setEnabled(true);
                }
            }
        });
        jmFile.add(jmiOpen);

        jmFile.add(jmiSave);

        jmFile.addSeparator();

        jmiExit = new JMenuItem("Выход");
        jmiExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!fileSaved)
                    saveOnCloseDialog();
                System.exit(0);
            }
        });
        jmFile.add(jmiExit);


        jmBrush.setSize(new Dimension(10, 10));
        URL imageUrl = this.getClass().getResource(".\\src\\ru\\burningGlobe\\generator\\images\\brush.png");
        if (imageUrl != null) {
            jmBrush.setText("");
            jmBrush.setIcon(new ImageIcon(imageUrl));
        }
        jmBrush.setEnabled(false);
        jmbMenu.add(jmBrush);

                                                                                                                        /** ALL TYPES OF BRUSHES **/

        jmiBrushRoad = new JMenuItem("Дорога");
        jmiBrushRoad.setBackground(GeneratorColors.roadColor);
        jmiBrushRoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jpMap.setCurrentBrush(1);
            }
        });
        jmBrush.add(jmiBrushRoad);

        jmiBrushDesert = new JMenuItem("Пустыня");
        jmiBrushDesert.setBackground(GeneratorColors.desertColor);
        jmiBrushDesert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jpMap.setCurrentBrush(2);
            }
        });
        jmBrush.add(jmiBrushDesert);

        jmiBrushStone = new JMenuItem("Камень");
        jmiBrushStone.setBackground(GeneratorColors.stoneColor);
        jmiBrushStone.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jpMap.setCurrentBrush(3);
            }
        });
        jmBrush.add(jmiBrushStone);

        jmiBrushRb = new JMenuItem("Рубидий");
        jmiBrushRb.setBackground(GeneratorColors.rbColor);
        jmiBrushRb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jpMap.setCurrentBrush(4);
            }
        });
        jmBrush.add(jmiBrushRb);

        jmiBrushNexus = new JMenuItem("Нексус");
        jmiBrushNexus.setBackground(GeneratorColors.nexusColor);
        jmiBrushNexus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jpMap.setCurrentBrush(5);
            }
        });
        jmBrush.add(jmiBrushNexus);

        jmBrush.addSeparator();

        jmiBrushEraser = new JMenuItem("Ластик");
        jmiBrushEraser.setBackground(GeneratorColors.eraserColor);
        jmiBrushEraser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jpMap.setCurrentBrush(-1);
            }
        });
        jmBrush.add(jmiBrushEraser);


        setVisible(true);
    }

    static void setFileAsUnsaved() {fileSaved = false;}

    private void saveFile() {
        if (fileSaved)
            return;
        if (file == null) {
            JFileChooser fileChooser;
            FileFilter filter;
            int returnVal;

            fileChooser = new JFileChooser();
            filter = new FileNameExtensionFilter("TD Map file", "tdmap");
            fileChooser.setFileFilter(filter);
            returnVal = fileChooser.showSaveDialog(MainWindow.this);

            if (returnVal == JFileChooser.APPROVE_OPTION)
                file = fileChooser.getSelectedFile();
        }
        try {
            FileWriter fw;
            StringBuilder sb;
            String filename;

            filename = file.toString();
            if (!filename.contains(".tdmap"))
                filename += ".tdmap";
            fw = new FileWriter(filename);
            sb = new StringBuilder();
            sb.append(jpMap.getXLinesCount()).append(" ").append(jpMap.getYLinesCount()).append("\r\n");
            int[][] mapArray = jpMap.getMap();
            for (int i = 0; i < jpMap.getYLinesCount(); i++) {
                for (int j = 0; j < jpMap.getXLinesCount(); j++) {
                    sb.append(mapArray[i][j]).append(" ");
                }
                sb.append("\r\n");
            }
            fw.write(sb.toString());
            fw.close();
            fileSaved = true;
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void openFile() {
        try {
            JFileChooser fileChooser;
            FileFilter filter;
            int returnVal;

            fileChooser = new JFileChooser();
            filter = new FileNameExtensionFilter("TD Map file","tdmap");
            fileChooser.setFileFilter(filter);
            returnVal = fileChooser.showOpenDialog(MainWindow.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
                fileSaved = false;
            }

            FileReader fr;
            int xCount, yCount;
            StringBuilder fileData;
            String sFile, s;
            String[] lineElements;
            int[][] map;

            fr = new FileReader(file);
            fileData = new StringBuilder();
            int symbol;
            while ((symbol = fr.read()) != -1) {
                fileData.append((char)symbol);
            }
            fr.close();

            sFile = fileData.toString();
            lineElements = sFile.substring(0, fileData.indexOf("\r\n")).split(" ");
            xCount = Integer.parseInt(lineElements[0]);
            yCount = Integer.parseInt(lineElements[1]);
            map = new int[yCount][xCount];
            for (int i = 0; i < yCount; i++) {
                sFile = sFile.substring(sFile.indexOf("\n") + 1);
                lineElements = sFile.substring(0, sFile.indexOf("\r\n")).split(" ");
                for (int j = 0; j < xCount; j++) {
                    map[i][j] = Integer.parseInt(lineElements[j]);
                }
            }
            jpMap = new Map(xCount, yCount);
            add(jpMap, BorderLayout.CENTER);
            jpMap.createNewMap(map);
            fileSaved = false;
            revalidate();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveOnCloseDialog() {
        int returnVal;
        returnVal = JOptionPane.showConfirmDialog(
                MainWindow.this,
                "Сохранить карту?",
                "Сохранение",
                JOptionPane.YES_NO_OPTION
        );
        if (returnVal == JOptionPane.YES_OPTION) {
            saveFile();
            fileSaved = true;
        }
        else if (returnVal == JOptionPane.NO_OPTION) {
            fileSaved = true;
        }
    }

    private boolean saveDialog() {
        if (file == null && fileSaved) {
            JOptionPane.showMessageDialog(MainWindow.this, "Карта еще не открыта");
            return false;
        }
        return true;
    }
}