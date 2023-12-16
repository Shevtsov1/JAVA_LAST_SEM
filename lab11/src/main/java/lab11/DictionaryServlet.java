package main.java.lab11;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DictionaryServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DictionaryQueryExecutor queryExecutor;

    @Override
    public void init() throws ServletException {
        super.init();
        queryExecutor = new DictionaryQueryExecutor();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String word = request.getParameter("word");
        String language = request.getParameter("language");

        String translation = queryExecutor.getTranslation(word, language);

        out.println("<html><body>");
        out.println("<h2>Dictionary</h2>");
        out.println("<form action=\"\" method=\"get\">");
        out.println("Word: <input type=\"text\" name=\"word\"><br>");
        out.println("Language: <input type=\"text\" name=\"language\"><br>");
        out.println("<input type=\"submit\" value=\"Translate\">");
        out.println("</form>");

        if (translation != null) {
            out.println("<h3>Translation: " + translation + "</h3>");
        }

        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String word = request.getParameter("word");
        String translation = request.getParameter("translation");
        String language = request.getParameter("language");

        queryExecutor.addWord(word, translation, language);

        out.println("<html><body>");
        out.println("<h2>Word Added</h2>");
        out.println("<p>Word: " + word + "</p>");
        out.println("<p>Translation: " + translation + "</p>");
        out.println("<p>Language: " + language + "</p>");
        out.println("</body></html>");
    }
}