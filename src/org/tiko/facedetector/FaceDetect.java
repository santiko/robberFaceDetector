package org.tiko.facedetector;

import static java.awt.Frame.NORMAL;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

public class FaceDetect extends javax.swing.JFrame {

    private thread thread = null;
    VideoCapture cap = null;
    //Mat frame = new Mat();
    MatOfRect wajah = new MatOfRect();
    MatOfByte mob = new MatOfByte();
    String ekstensiFile = ".png";
    CascadeClassifier faceDetect = new CascadeClassifier("haarcascade_frontalface_alt.xml");

    Mat matriks = new Mat();

    class thread implements Runnable {

        @Override
        public void run() {
            {
                while (true) {
                    if (cap.isOpened()) {
                        try {
                            cap.retrieve(matriks);
                            Graphics g = jPanel1.getGraphics();
                            faceDetect.detectMultiScale(matriks, wajah);
                            System.out.println(String.format("Jumlah wajah terdeteksi: %d", wajah.toArray().length));

                            String text = String.format("Jumlah wajah terdeteksi: %d", wajah.toArray().length);

                            Imgproc.putText(matriks, text, new Point(15, 25
                            ), NORMAL, 0.7, new Scalar(255, 255, 255), 2);

                            // Imgproc.putText(matriks, Date, new Point(15, 35
                            // ), Core.FONT_HERSHEY_COMPLEX_SMALL, 0.5, new Scalar(0, 255, 0));
                            for (Rect rect : wajah.toArray()) {
                                Imgproc.rectangle(matriks,
                                        new Point(rect.x, rect.y), new Point(
                                                rect.x + rect.width, rect.y
                                                + rect.height),
                                        new Scalar(0, 255, 0), 2);
                                Imgproc.putText(matriks, String.format("%d x %d", rect.width, rect.height), new Point(rect.x + 30, rect.y + rect.height + 20
                                ), NORMAL, 0.5, new Scalar(0, 255, 0));
                                // if(rect.x)
                                Imgproc.putText(matriks, "Terdeteksi 1 wajah", new Point(rect.x, rect.y - 10
                                ), NORMAL, 0.5, new Scalar(0, 255, 0));

                                Imgproc.putText(matriks, String.format("%d feet dari kamera..", (350 / ((rect.height)))), new Point(rect.x, rect.y + rect.height + 40
                                ), NORMAL, 0.5, new Scalar(0, 255, 0));
                            }
                            Imgcodecs.imencode(ekstensiFile, matriks, mob);
                            byte[] byteArray = mob.toArray();
                            BufferedImage buff = null;

                            InputStream in;
                            in = new ByteArrayInputStream(
                                    byteArray);
                            buff = ImageIO.read(in);

                            g.drawImage(buff, 0, 0, null);

                        } catch (IOException ex) {
                            Logger.getLogger(FaceDetect.class.getName()).log(Level.SEVERE, null, ex);
                            //System.out.println("Ada kesalahan: " + ex.printStackTrace());
                        }
                    }
                }
            }
        }
    }

    public FaceDetect() {
        start();
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));
        jPanel1.setName("FaceDetect "); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 630, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 405, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void start() {
        cap = new VideoCapture(0);
        thread = new thread();
        Thread t = new Thread(thread);
        t.setDaemon(true);
        t.start();
    }

    public static void main(String args[]) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("Pustaka opencv2.x berhasil dibuka!");

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FaceDetect frame = new FaceDetect();
                frame.setTitle("FaceDetect");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
