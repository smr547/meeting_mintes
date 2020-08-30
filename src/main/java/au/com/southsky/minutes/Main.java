package au.com.southsky.minutes;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.itextpdf.html2pdf.HtmlConverter;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.kohsuke.github.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws IOException {
        // Markdown stylesheet courtesy:
        // https://github.com/simonlc/Markdown-CSS/blob/master/markdown.css
        String style = "<head><style>html {\n" +
                "  font-size: 100%;\n" +
                "  overflow-y: scroll;\n" +
                "  -webkit-text-size-adjust: 100%;\n" +
                "  -ms-text-size-adjust: 100%;\n" +
                "}\n" +
                "\n" +
                "body {\n" +
                "  color: #444;\n" +
                "  font-family: Georgia, Palatino, 'Palatino Linotype', Times, 'Times New Roman', serif;\n" +
                "  font-size: 12px;\n" +
                "  line-height: 1.5em;\n" +
                "  padding: 1em;\n" +
                "  margin: auto;\n" +
                "  max-width: 42em;\n" +
                "  background: #fefefe;\n" +
                "}\n" +
                "\n" +
                "a {\n" +
                "  color: #0645ad;\n" +
                "  text-decoration: none;\n" +
                "}\n" +
                "\n" +
                "a:visited {\n" +
                "  color: #0b0080;\n" +
                "}\n" +
                "\n" +
                "a:hover {\n" +
                "  color: #06e;\n" +
                "}\n" +
                "\n" +
                "a:active {\n" +
                "  color: #faa700;\n" +
                "}\n" +
                "\n" +
                "a:focus {\n" +
                "  outline: thin dotted;\n" +
                "}\n" +
                "\n" +
                "a:hover,\n" +
                "a:active {\n" +
                "  outline: 0;\n" +
                "}\n" +
                "\n" +
                "::-moz-selection {\n" +
                "  background: rgba(255, 255, 0, 0.3);\n" +
                "  color: #000;\n" +
                "}\n" +
                "\n" +
                "::selection {\n" +
                "  background: rgba(255, 255, 0, 0.3);\n" +
                "  color: #000;\n" +
                "}\n" +
                "\n" +
                "a::-moz-selection {\n" +
                "  background: rgba(255, 255, 0, 0.3);\n" +
                "  color: #0645ad;\n" +
                "}\n" +
                "\n" +
                "a::selection {\n" +
                "  background: rgba(255, 255, 0, 0.3);\n" +
                "  color: #0645ad;\n" +
                "}\n" +
                "\n" +
                "p {\n" +
                "  margin: 1em 0;\n" +
                "}\n" +
                "\n" +
                "img {\n" +
                "  max-width: 100%;\n" +
                "}\n" +
                "\n" +
                "h1,\n" +
                "h2,\n" +
                "h3,\n" +
                "h4,\n" +
                "h5,\n" +
                "h6 {\n" +
                "  font-weight: normal;\n" +
                "  color: #111;\n" +
                "  line-height: 1em;\n" +
                "}\n" +
                "\n" +
                "h4,\n" +
                "h5,\n" +
                "h6 {\n" +
                "  font-weight: bold;\n" +
                "}\n" +
                "\n" +
                "h1 {\n" +
                "  font-size: 2.5em;\n" +
                "}\n" +
                "\n" +
                "h2 {\n" +
                "  font-size: 2em;\n" +
                "}\n" +
                "\n" +
                "h3 {\n" +
                "  font-size: 1.5em;\n" +
                "}\n" +
                "\n" +
                "h4 {\n" +
                "  font-size: 1.2em;\n" +
                "}\n" +
                "\n" +
                "h5 {\n" +
                "  font-size: 1em;\n" +
                "}\n" +
                "\n" +
                "h6 {\n" +
                "  font-size: 0.9em;\n" +
                "}\n" +
                "\n" +
                "blockquote {\n" +
                "  color: #666666;\n" +
                "  margin: 0;\n" +
                "  padding-left: 3em;\n" +
                "  border-left: 0.5em #eee solid;\n" +
                "}\n" +
                "\n" +
                "hr {\n" +
                "  display: block;\n" +
                "  border: 0;\n" +
                "  border-top: 1px solid #aaa;\n" +
                "  border-bottom: 1px solid #eee;\n" +
                "  margin: 1em 0;\n" +
                "  padding: 0;\n" +
                "}\n" +
                "\n" +
                "pre,\n" +
                "code,\n" +
                "kbd,\n" +
                "samp {\n" +
                "  color: #000;\n" +
                "  font-family: monospace, monospace;\n" +
                "  _font-family: 'courier new', monospace;\n" +
                "  font-size: 0.98em;\n" +
                "}\n" +
                "\n" +
                "pre {\n" +
                "  white-space: pre;\n" +
                "  white-space: pre-wrap;\n" +
                "  word-wrap: break-word;\n" +
                "}\n" +
                "\n" +
                "b,\n" +
                "strong {\n" +
                "  font-weight: bold;\n" +
                "}\n" +
                "\n" +
                "dfn {\n" +
                "  font-style: italic;\n" +
                "}\n" +
                "\n" +
                "ins {\n" +
                "  background: #ff9;\n" +
                "  color: #000;\n" +
                "  text-decoration: none;\n" +
                "}\n" +
                "\n" +
                "mark {\n" +
                "  background: #ff0;\n" +
                "  color: #000;\n" +
                "  font-style: italic;\n" +
                "  font-weight: bold;\n" +
                "}\n" +
                "\n" +
                "sub,\n" +
                "sup {\n" +
                "  font-size: 75%;\n" +
                "  line-height: 0;\n" +
                "  position: relative;\n" +
                "  vertical-align: baseline;\n" +
                "}\n" +
                "\n" +
                "sup {\n" +
                "  top: -0.5em;\n" +
                "}\n" +
                "\n" +
                "sub {\n" +
                "  bottom: -0.25em;\n" +
                "}\n" +
                "\n" +
                "ul,\n" +
                "ol {\n" +
                "  margin: 1em 0;\n" +
                "  padding: 0 0 0 2em;\n" +
                "}\n" +
                "\n" +
                "li p:last-child {\n" +
                "  margin: 0;\n" +
                "}\n" +
                "\n" +
                "dd {\n" +
                "  margin: 0 0 0 2em;\n" +
                "}\n" +
                "\n" +
                "img {\n" +
                "  border: 0;\n" +
                "  -ms-interpolation-mode: bicubic;\n" +
                "  vertical-align: middle;\n" +
                "}\n" +
                "\n" +
                "table {\n" +
                "  border-collapse: collapse;\n" +
                "  border-spacing: 0;\n" +
                "}\n" +
                "\n" +
                "td {\n" +
                "  vertical-align: top;\n" +
                "}\n" +
                "\n" +
                "@media only screen and (min-width: 480px) {\n" +
                "  body {\n" +
                "    font-size: 14px;\n" +
                "  }\n" +
                "}\n" +
                "\n" +
                "@media only screen and (min-width: 768px) {\n" +
                "  body {\n" +
                "    font-size: 16px;\n" +
                "  }\n" +
                "}\n" +
                "\n" +
                "@media print {\n" +
                "  * {\n" +
                "    background: transparent !important;\n" +
                "    color: black !important;\n" +
                "    filter: none !important;\n" +
                "    -ms-filter: none !important;\n" +
                "  }\n" +
                "\n" +
                "  body {\n" +
                "    font-size: 12pt;\n" +
                "    max-width: 100%;\n" +
                "  }\n" +
                "\n" +
                "  a,\n" +
                "  a:visited {\n" +
                "    text-decoration: underline;\n" +
                "  }\n" +
                "\n" +
                "  hr {\n" +
                "    height: 1px;\n" +
                "    border: 0;\n" +
                "    border-bottom: 1px solid black;\n" +
                "  }\n" +
                "\n" +
                "  a[href]:after {\n" +
                "    content: \" (\" attr(href) \")\";\n" +
                "  }\n" +
                "\n" +
                "  abbr[title]:after {\n" +
                "    content: \" (\" attr(title) \")\";\n" +
                "  }\n" +
                "\n" +
                "  .ir a:after,\n" +
                "  a[href^=\"javascript:\"]:after,\n" +
                "  a[href^=\"#\"]:after {\n" +
                "    content: \"\";\n" +
                "  }\n" +
                "\n" +
                "  pre,\n" +
                "  blockquote {\n" +
                "    border: 1px solid #999;\n" +
                "    padding-right: 1em;\n" +
                "    page-break-inside: avoid;\n" +
                "  }\n" +
                "\n" +
                "  tr,\n" +
                "  img {\n" +
                "    page-break-inside: avoid;\n" +
                "  }\n" +
                "\n" +
                "  img {\n" +
                "    max-width: 100% !important;\n" +
                "  }\n" +
                "\n" +
                "  @page :left {\n" +
                "    margin: 15mm 20mm 15mm 10mm;\n" +
                "  }\n" +
                "\n" +
                "  @page :right {\n" +
                "    margin: 15mm 10mm 15mm 20mm;\n" +
                "  }\n" +
                "\n" +
                "  p,\n" +
                "  h2,\n" +
                "  h3 {\n" +
                "    orphans: 3;\n" +
                "    widows: 3;\n" +
                "  }\n" +
                "\n" +
                "  h2,\n" +
                "  h3 {\n" +
                "    page-break-after: avoid;\n" +
                "  }\n" +
                "}</style></head>";

        // String style = "<head></style>"+
        //        "<link rel=\"stylesheet\" type=\"text/css\" href=\"https://gist.githubusercontent.com/andyferra/2554919/raw/10ce87fe71b23216e3075d5648b8b9e56f7758e1/github.css\">"+
        //        "</style>"+
        //        "</head>";
        Options o = new Options();
        JCommander c = new JCommander(o);
        c.setProgramName(Main.class.getSimpleName());

        try {
            c.parse(args);
        } catch (ParameterException e) {
            System.out.println(e.getMessage());
            c.usage();
            System.exit(1);
        }

        GitHub h = GitHub.connect();
        GHUser user = h.getUser(o.username);
        GHRepository repo = user.getRepository(o.repositoryName);

        StringBuilder builder = new StringBuilder(style)
                .append("<h1>")
                .append(o.repositoryName)
                .append(" meeting ")
                .append(o.label)
                .append("</h1>")
                .append("<p>Issues labelled as \"")
                .append(o.label)
                .append("\" with comments recorded after ")
                .append(o.fromDate.toString())
                .append("</p><ul>");

        // GHIssue issue = repo.getIssue(345);
        ArrayList<GHIssue> agendaIssues = new ArrayList<>();
        for (GHIssue issue : repo.getIssues(GHIssueState.ALL)) {
            if (issue.getLabels().stream().map(GHLabel::getName).anyMatch(s -> s.equals(o.label))) {
                agendaIssues.add(issue);
            }
        }

        // sort the agenda items
        if (o.label.equals("Agenda")) {
            agendaIssues.sort(Comparator.comparing(GHIssue::getTitle));
        }

        for (GHIssue issue : agendaIssues) {

            System.out.println("Issue " + issue.getNumber());
            builder.append("<h2>");
            builder.append(issue.getTitle());
            builder.append("</h2>");

            for (GHIssueComment comment : issue.getComments()) {
                String body = comment.getBody();
                if (!body.isEmpty() && comment.getCreatedAt().after(o.fromDate)) {
                    if (!o.noTimestamp) {
                        builder
                                .append("<small>(Comment by ")
                                .append(comment.getUser().getName())
                                .append(" on ")
                                .append(comment.getCreatedAt())
                                .append("):</small>");
                    }
                    Parser mdParser = Parser.builder().build();
                    Node mdDoc = mdParser.parse(body);
                    HtmlRenderer renderer = HtmlRenderer.builder().build();
                    builder.append(renderer.render(mdDoc));
                }
            }
        }

        builder.append("</ul>");

        try (FileOutputStream pdfFile = new FileOutputStream("/tmp/out.pdf")) {
            HtmlConverter.convertToPdf(builder.toString(), pdfFile);
        }
    }

    public static class Options {
        @Parameter(
                required = true,
                names = {"-u", "--username"},
                description = "The GitHub username"
        )
        String username;

        @Parameter(
                required = true,
                names = {"-r", "--repo"},
                description = "The GitHub repo"
        )
        String repositoryName;

        @Parameter(
                required = true,
                names = {"-l", "--label"},
                description = "The issues to extract"
        )
        String label;

        @Parameter(
                required = true,
                names = {"-f", "--from_date"},
                description = "Include comments from date"
        )
        Date fromDate;

        @Parameter(
                required = false,
                names = {"-n", "--no_timestamp"},
                description = "Don't include comments on timestamps"
        )
        boolean noTimestamp = false;
    }
}
