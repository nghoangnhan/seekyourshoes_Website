package vn.hcmute.seekyourshoes.controller.ShoppingCart;

import vn.hcmute.seekyourshoes.DAO.InvoiceDAO;
import vn.hcmute.seekyourshoes.model.CartItem;
import vn.hcmute.seekyourshoes.model.Invoice;
import vn.hcmute.seekyourshoes.model.ShoppingCart;
import vn.hcmute.seekyourshoes.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet(urlPatterns = {"/checkOut"})
public class checkOut extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        ShoppingCart shoppingCart = (ShoppingCart) session.getAttribute("shoppingCart");
        User user = (User) session.getAttribute("loggedUser");

        String address = req.getParameter("deliveryAddress");
        Invoice invoice = new Invoice();
        invoice.setItems(shoppingCart.getItems());
        invoice.setDeliveryAddress(address);
        invoice.setUser(user);
        invoice.setTotalPay(shoppingCart.getTotalPay());
        System.out.println(shoppingCart.getTotalPay());

        try {
            InvoiceDAO.getInstance().addInvoice(invoice);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ShoppingCart newCart = new ShoppingCart();
        newCart.setUserId(user.getUserId());
        Date date = new Date();
        newCart.setPurchasedDate(date);
        List<CartItem> items = new ArrayList<>();
        newCart.setItems(items);
        session.setAttribute("shoppingCart", newCart);

        getServletContext().getRequestDispatcher("/loadProfile").forward(req, resp);

    }
}
