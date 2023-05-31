package vn.hcmute.seekyourshoes.controller.ShoppingCart;

import vn.hcmute.seekyourshoes.DAO.ProductDAO;
import vn.hcmute.seekyourshoes.model.CartItem;
import vn.hcmute.seekyourshoes.model.Product;
import vn.hcmute.seekyourshoes.model.ShoppingCart;
import vn.hcmute.seekyourshoes.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@WebServlet(urlPatterns = {"/addToCart"})
public class addToCart extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = "/view/login.jsp";

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("loggedUser");

        if (user == null) {
            getServletContext().getRequestDispatcher(url).forward(req, resp);
        }
        else {

            Integer productId = Integer.parseInt(req.getParameter("productId"));
            Product product;
            try {
                product = ProductDAO.getInstance().getProductById(productId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


            CartItem item = new CartItem();
            item.setProduct(product);
            item.setAmount(1);

            System.out.println(item.getProduct().getProductId());

            ShoppingCart shoppingCart = getSessionCart(req, resp);
            if(shoppingCart == null) {
                shoppingCart = new ShoppingCart();
                shoppingCart.setUserId(user.getUserId());
                Date date = new Date();
                shoppingCart.setPurchasedDate(date);
                List<CartItem> items = new ArrayList<>();
                shoppingCart.setItems(items);
                shoppingCart.setTotalPay(0.0);

                shoppingCart = addItemToCart(shoppingCart, item);
            }
            else {
                shoppingCart = addItemToCart(shoppingCart, item);
            }
            session.setAttribute("shoppingCart", shoppingCart);
        }

    }

    ShoppingCart addItemToCart(ShoppingCart shoppingCart, CartItem item) {
        List<CartItem> items = shoppingCart.getItems();
        boolean isItemExisted = false;

        //check if the product has already been in ShoppingCart
        //then update amount ++

        if(items != null) {
            for (CartItem i : items) {
                if (i.getProduct().getProductId() == item.getProduct().getProductId()) {
                    i.setAmount(i.getAmount() + 1);
                    shoppingCart.setTotalPay(shoppingCart.getTotalPay() + i.getProduct().getPrice());
                    isItemExisted = true;
                }
            }
        }

        //add new if the product is not in ShoppingCart
        if(!isItemExisted) {
            items.add(item);
            shoppingCart.setTotalPay(shoppingCart.getTotalPay() + item.getProduct().getPrice());
        }
        shoppingCart.setItems(items);
        return shoppingCart;
    }

    ShoppingCart getSessionCart (HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        ShoppingCart shoppingCart = (ShoppingCart) session.getAttribute("shoppingCart");
        return shoppingCart;
    }
}
