package ru.itis.merch.store.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@WebServlet("/resources/*")
public class ResourcesServlet extends HttpServlet {

    private String pathToFileDirectories;

    @Override
    public void init(ServletConfig config) throws ServletException {
        pathToFileDirectories = (String) config.getServletContext().getAttribute("pathToFile");
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String filePath = req.getPathInfo();
        if (filePath == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Path staticPath = Paths.get(pathToFileDirectories, filePath);

        if (!Files.exists(staticPath) || Files.isDirectory(staticPath)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try (InputStream inputStream = Files.newInputStream(staticPath);
             OutputStream outputStream = resp.getOutputStream()) {

            // Определяем тип контента
            String mimeType = getServletContext().getMimeType(filePath);
            resp.setContentType(Objects.requireNonNullElse(mimeType, "application/octet-stream"));

            // Копируем данные из файла в ответ
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error reading static file");
        }

    }
}
