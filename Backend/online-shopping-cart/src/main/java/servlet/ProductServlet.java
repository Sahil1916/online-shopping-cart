package servlet;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Product;
import service.ProductService;

@WebServlet("/api/products")
public class ProductServlet extends HttpServlet {

    private final ProductService productService = new ProductService();
    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);    
//    @Override
//    public void init() throws ServletException {
//        System.out.println("ProductServlet Loaded");
//    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	
        System.out.println("******** ProductServlet doGet ********");


        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String id = req.getParameter("id");

        if (id != null && !id.isBlank()) {

            Product product =
                    productService.getProductById(Long.parseLong(id));

            if (product == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            objectMapper.writeValue(resp.getWriter(), product);

        } else {

            List<Product> products =
                    productService.getAllProducts();

            objectMapper.writeValue(resp.getWriter(), products);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Product product =
                objectMapper.readValue(req.getReader(), Product.class);

        boolean saved =
                productService.addProduct(product);

        resp.setContentType("application/json");

        if (saved) {
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        objectMapper.writeValue(resp.getWriter(), saved);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Product product =
                objectMapper.readValue(req.getReader(), Product.class);

        boolean updated =
                productService.updateProduct(product);

        resp.setContentType("application/json");

        if (updated) {
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        objectMapper.writeValue(resp.getWriter(), updated);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String id = req.getParameter("id");

        if (id == null || id.isBlank()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        boolean deleted =
                productService.deleteProduct(Long.parseLong(id));

        resp.setContentType("application/json");

        if (deleted) {
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        objectMapper.writeValue(resp.getWriter(), deleted);
    }
}