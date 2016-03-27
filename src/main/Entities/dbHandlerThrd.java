package main.Entities;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.eclipse.jetty.server.Handler;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;

/**
 *
 * Receives requests and hands it to the next availible request thread
 * jetty documentation:  http://download.eclipse.org/jetty/stable-9/apidocs/
 * jetty tutorial:  https://www.eclipse.org/jetty/documentation/current/embedding-jetty.html
 */
public class dbHandlerThrd extends AbstractHandler {

    protected void doGet( HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>Hello from HelloServlet</h1>");


    }
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        String greeting = "gasdf";
        String body = null;
        PrintWriter out = response.getWriter();

        out.println("<h1>" + greeting + "</h1>");
        if (body != null)
        {
            out.println(body);
        }

        baseRequest.setHandled(true);
    }
}
