package com.zzp.pdf.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description pdf合并
 * @Author Garyzeng
 * @since 2019.10.10
 **/
public class PDFMergeUtils {

    /**
     * 合并文件夹下的pdf文件
     * @param fileDir
     * @throws Exception
     */
    public void merge(String fileDir) throws Exception {
        merge(fileDir, fileDir + File.separator + "merge.pdf");
    }

    /**
     * 合并文件夹下的pdf文件，并将合并后的内容输出到目标文件
     * @param fileDir
     * @param destinationFile
     * @throws Exception
     */
    public void merge(String fileDir, String destinationFile) throws Exception {
        File folder = new File(fileDir);
        if (!folder.exists()) {
            throw new FileNotFoundException(fileDir + "找不到对应目录");
        }

        if (!folder.isDirectory()) {
            throw new FileNotFoundException(fileDir + "不是目录");
        }

        List<PdfReader> readers = getPdfReaders(getFiles(folder));
        if (readers == null || readers.size() == 0) {
            throw new FileNotFoundException(fileDir + "该目录下不存在文件");
        }

        int totalPages = 0;
        for (int i = 0; i < readers.size(); i++) {
            totalPages += readers.get(i).getNumberOfPages();
        }

        FileOutputStream out = new FileOutputStream(destinationFile);
        Document document = new Document();

        PdfCopy copy = new PdfCopy(document, out);

        document.open();

        for(int i=0; i<readers.size(); i++)
        {
            PdfReader reader = readers.get(i);

            int n = reader.getNumberOfPages();

            for(int j=1; j<=n; j++)
            {
                document.newPage();
                PdfImportedPage page = copy.getImportedPage(reader, j);
                copy.addPage(page);
            }
        }

        document.close();

//        PdfWriter writer = PdfWriter.getInstance(document, out);
//
//        document.open();
//        PdfContentByte cb = writer.getDirectContent();
//
//        int pageOfCurrentReaderPDF = 0;
//        Iterator<PdfReader> iteratorPDFReader = readers.iterator();
//
//        document.setPageSize(PageSize.A4.rotate());
//        document.setMargins(0, 0, 0, -100);
//
//        // Loop through the PDF files and add to the output.
//        while (iteratorPDFReader.hasNext()) {
//            PdfReader pdfReader = iteratorPDFReader.next();
//
//            // Create a new page in the target for each source page.
//            while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
//                document.newPage();
//                pageOfCurrentReaderPDF++;
//                PdfImportedPage page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
//                cb.addTemplate(page, 0, 0);
//            }
//            pageOfCurrentReaderPDF = 0;
//        }
//        out.flush();
//        document.close();
//        out.close();
    }

    /**
     * 获取文件夹下的所有文件
     * @param folder
     * @return
     */
    public List<File> getFiles(File folder) {
        List<File> files = new ArrayList<File>();
        File[] subs = folder.listFiles();
        if (subs != null && subs.length > 0) {
            for (int i = 0; i < subs.length; i++) {
                File sub = subs[i];
                if (sub.isFile()) {
                    files.add(sub);
                }
            }
            return files;
        }
        return null;
    }

    /**
     * 将pdf文件转换为PdfReader列表
     * @param files
     * @return
     * @throws IOException
     */
    public List<PdfReader> getPdfReaders(List<File> files) throws IOException {
        if (files != null && files.size() > 0) {
            List<PdfReader> readers = new ArrayList<PdfReader>();
            for (int i = 0; i < files.size(); i++) {
                if (files.get(i).getName().contains(".pdf")) {
                    PdfReader reader = new PdfReader(files.get(i).getPath());
                    readers.add(reader);
                }
            }
            return readers;
        }
        return null;
    }

    public static void main(String[] args) {
        PDFMergeUtils pdfMerge = new PDFMergeUtils();
        try {
            pdfMerge.merge("E:\\pdfMergeTest\\20191011155027769\\pdf", "E:\\pdfMergeTest\\20191011155027769\\pdf\\merge.pdf");
            System.out.println("合并完毕");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
