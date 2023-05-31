package vn.hcmute.seekyourshoes.controller.ShoppingCart;

import vn.hcmute.seekyourshoes.DAO.ProductDAO;
import vn.hcmute.seekyourshoes.model.CartItem;
import vn.hcmute.seekyourshoes.model.Product;
import vn.hcmute.seekyourshoes.model.ShoppingCart;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/deleteCartItem"})
public class deleteCartItem extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Integer productId = Integer.parseInt(req.getParameter("productId"));

        ShoppingCart shoppingCart = getSessionCart(req, resp);


        for (int i = 0; i < shoppingCart.getItems().size(); i++) {
            CartItem item = shoppingCart.getItems().get(i);
            if (item.getProduct().getProductId().equals(productId)) {
                shoppingCart.getItems().remove(item);
            }
        }
        HttpSession session = req.getSession();
        req.setAttribute("shoppingCart", shoppingCart);
    }

    ShoppingCart getSessionCart (HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        ShoppingCart cart = (ShoppingCart) session.getAttribute("shoppingCart");
        return cart;
    }
}
