from pathlib import Path

from reportlab.lib import colors
from reportlab.lib.enums import TA_CENTER, TA_LEFT
from reportlab.lib.pagesizes import LETTER
from reportlab.lib.styles import ParagraphStyle, getSampleStyleSheet
from reportlab.lib.units import inch
from reportlab.platypus import (
    ListFlowable,
    ListItem,
    PageBreak,
    Paragraph,
    Preformatted,
    SimpleDocTemplate,
    Spacer,
    Table,
    TableStyle,
)


OUTPUT_PDF = Path("Wedding-Package-Management-New-PC-Setup-Guide.pdf")


def build_styles():
    styles = getSampleStyleSheet()
    styles.add(
        ParagraphStyle(
            name="GuideTitle",
            parent=styles["Title"],
            fontName="Helvetica-Bold",
            fontSize=22,
            leading=27,
            textColor=colors.HexColor("#1B3F6B"),
            alignment=TA_LEFT,
            spaceAfter=8,
        )
    )
    styles.add(
        ParagraphStyle(
            name="GuideSubtitle",
            parent=styles["BodyText"],
            fontName="Helvetica",
            fontSize=10.5,
            leading=14,
            textColor=colors.HexColor("#666666"),
            spaceAfter=14,
        )
    )
    styles.add(
        ParagraphStyle(
            name="GuideH1",
            parent=styles["Heading1"],
            fontName="Helvetica-Bold",
            fontSize=14,
            leading=18,
            textColor=colors.HexColor("#1B3F6B"),
            spaceBefore=10,
            spaceAfter=6,
        )
    )
    styles.add(
        ParagraphStyle(
            name="GuideBody",
            parent=styles["BodyText"],
            fontName="Helvetica",
            fontSize=10.5,
            leading=14,
            textColor=colors.HexColor("#222222"),
            spaceAfter=8,
        )
    )
    styles.add(
        ParagraphStyle(
            name="GuideCode",
            parent=styles["Code"],
            fontName="Courier",
            fontSize=9,
            leading=11,
            backColor=colors.HexColor("#F5F7FA"),
            borderPadding=(6, 6, 6),
            borderColor=colors.HexColor("#D9E1EA"),
            borderWidth=0.5,
            borderRadius=2,
            spaceAfter=8,
            leftIndent=10,
        )
    )
    styles.add(
        ParagraphStyle(
            name="GuideSmall",
            parent=styles["BodyText"],
            fontName="Helvetica",
            fontSize=9,
            leading=12,
            textColor=colors.HexColor("#666666"),
            alignment=TA_CENTER,
        )
    )
    return styles


def code_block(text, style):
    return Preformatted(text, style)


def bullet_list(items, style):
    flow = []
    for item in items:
        flow.append(ListItem(Paragraph(item, style)))
    return ListFlowable(flow, bulletType="bullet", start="circle", leftIndent=18)


def number_list(items, style):
    flow = []
    for item in items:
        flow.append(ListItem(Paragraph(item, style)))
    return ListFlowable(flow, bulletType="1", leftIndent=18)


def add_page_chrome(canvas, doc):
    canvas.saveState()
    canvas.setFont("Helvetica", 9)
    canvas.setFillColor(colors.HexColor("#777777"))
    canvas.drawString(doc.leftMargin, LETTER[1] - 0.55 * inch, "Wedding Package Management | New PC Setup Guide")
    canvas.setFillColor(colors.HexColor("#999999"))
    canvas.drawCentredString(LETTER[0] / 2, 0.45 * inch, f"Page {canvas.getPageNumber()}")
    canvas.restoreState()


styles = build_styles()
story = []

story.append(Paragraph("Wedding Package Management<br/>New PC Setup Guide", styles["GuideTitle"]))
story.append(
    Paragraph(
        "Use this guide to set up the project on a brand-new Windows PC, configure MySQL, build the WAR file, deploy it to Tomcat 9, and log in successfully.",
        styles["GuideSubtitle"],
    )
)

summary = Table(
    [
        ["Project type", "Java web application using JSP, Servlets, JDBC, Maven, and Tomcat 9"],
        ["Database", "MySQL schema: wedding_package_management"],
        ["WAR file", r"target\wedding-package-management.war"],
        ["Default logins", "admin/admin, organizer1/organizer1, organizer2/organizer2"],
    ],
    colWidths=[1.65 * inch, 4.95 * inch],
)
summary.setStyle(
    TableStyle(
        [
            ("BACKGROUND", (0, 0), (0, -1), colors.HexColor("#EEF3F8")),
            ("TEXTCOLOR", (0, 0), (-1, -1), colors.HexColor("#1F1F1F")),
            ("FONTNAME", (0, 0), (0, -1), "Helvetica-Bold"),
            ("FONTNAME", (1, 0), (1, -1), "Helvetica"),
            ("FONTSIZE", (0, 0), (-1, -1), 10),
            ("LEADING", (0, 0), (-1, -1), 13),
            ("BOX", (0, 0), (-1, -1), 0.6, colors.HexColor("#D9D9D9")),
            ("INNERGRID", (0, 0), (-1, -1), 0.5, colors.HexColor("#D9D9D9")),
            ("VALIGN", (0, 0), (-1, -1), "MIDDLE"),
            ("LEFTPADDING", (0, 0), (-1, -1), 8),
            ("RIGHTPADDING", (0, 0), (-1, -1), 8),
            ("TOPPADDING", (0, 0), (-1, -1), 7),
            ("BOTTOMPADDING", (0, 0), (-1, -1), 7),
        ]
    )
)
story.append(summary)
story.append(Spacer(1, 0.18 * inch))

sections = [
    (
        "1. Software You Need",
        bullet_list(
            [
                "Java JDK 11",
                "Apache Maven",
                "MySQL Server 8.x",
                "Apache Tomcat 9.x",
                "Optional: MySQL Workbench for easier database management",
            ],
            styles["GuideBody"],
        ),
    ),
    (
        "2. Copy the Project",
        Paragraph(
            r"Copy the full project folder to the new PC. Example location: D:\Projects\Wedding Package Management. You can use another folder, but update the commands below to match your actual path.",
            styles["GuideBody"],
        ),
    ),
    (
        "3. Verify Java and Maven",
        [
            Paragraph("Open PowerShell and run these commands:", styles["GuideBody"]),
            code_block("java -version\nmvn -version", styles["GuideCode"]),
        ],
    ),
    (
        "4. Start MySQL Server",
        [
            Paragraph(
                "If MySQL was installed as a Windows service, verify it is running. The common service name is MySQL80, but your PC may show a different service name.",
                styles["GuideBody"],
            ),
            code_block("sc query MySQL80\nnet start MySQL80", styles["GuideCode"]),
        ],
    ),
    (
        "5. Create the Database",
        [
            Paragraph(
                "Run the schema file included in the project. It creates the wedding_package_management database and inserts sample event and venue data.",
                styles["GuideBody"],
            ),
            code_block(
                'mysql -u root -p < "D:\\Projects\\Wedding Package Management\\db-schema.sql"\n'
                '& "C:\\Program Files\\MySQL\\MySQL Server 8.4\\bin\\mysql.exe" -u root -p < "D:\\Projects\\Wedding Package Management\\db-schema.sql"',
                styles["GuideCode"],
            ),
        ],
    ),
    (
        "6. Update Database Password if Needed",
        Paragraph(
            r"The application reads database credentials from src\main\webapp\WEB-INF\web.xml. The current defaults are root / password. If your MySQL password is different, edit jdbc.username or jdbc.password before building.",
            styles["GuideBody"],
        ),
    ),
    (
        "7. Build the Project",
        [
            Paragraph("Go to the project folder and build the WAR file with Maven:", styles["GuideBody"]),
            code_block('cd "D:\\Projects\\Wedding Package Management"\nmvn clean package', styles["GuideCode"]),
        ],
    ),
    (
        "8. Set Tomcat Variables and Deploy",
        [
            Paragraph(
                "Update the sample paths below to your real JDK 11 and Tomcat 9 installation folders, then copy the built WAR into Tomcat webapps.",
                styles["GuideBody"],
            ),
            code_block(
                '$env:JAVA_HOME="C:\\Program Files\\Microsoft\\jdk-11.0.x"\n'
                '$env:CATALINA_HOME="C:\\apache-tomcat-9.0.xxx"\n'
                'Copy-Item "D:\\Projects\\Wedding Package Management\\target\\wedding-package-management.war" "$env:CATALINA_HOME\\webapps\\"',
                styles["GuideCode"],
            ),
        ],
    ),
    (
        "9. Start Tomcat and Open the App",
        [
            code_block('cd "$env:CATALINA_HOME\\bin"\n.\\startup.bat', styles["GuideCode"]),
            Paragraph("Wait 10 to 20 seconds, then open: http://localhost:8080/wedding-package-management/", styles["GuideBody"]),
        ],
    ),
    (
        "10. Login Credentials",
        bullet_list(
            [
                "Admin: admin / admin",
                "Organizer: organizer1 / organizer1",
                "Organizer: organizer2 / organizer2",
            ],
            styles["GuideBody"],
        ),
    ),
    (
        "11. Quick Troubleshooting",
        number_list(
            [
                "If the browser shows a database error, confirm MySQL is running and the password in web.xml matches your real MySQL password.",
                "If the browser shows 404, confirm wedding-package-management.war was copied into Tomcat webapps and Tomcat started successfully.",
                "If Tomcat fails to start, make sure you are using Tomcat 9, not Tomcat 10 or 11.",
                "If mvn is not recognized, add Maven bin to PATH and open a new PowerShell window.",
                "If java is not recognized, fix JAVA_HOME and PATH, then reopen PowerShell.",
            ],
            styles["GuideBody"],
        ),
    ),
    (
        "12. Final Run Order",
        number_list(
            [
                "Install Java 11, Maven, MySQL Server, and Tomcat 9.",
                "Copy the project to the new PC.",
                "Start MySQL Server.",
                "Run db-schema.sql.",
                "Update web.xml if your DB password is different.",
                "Run mvn clean package.",
                "Copy the WAR file to Tomcat webapps.",
                "Start Tomcat.",
                "Open the application and log in.",
            ],
            styles["GuideBody"],
        ),
    ),
]

for title, content in sections:
    story.append(Paragraph(title, styles["GuideH1"]))
    if isinstance(content, list):
        story.extend(content)
    else:
        story.append(content)

story.append(Spacer(1, 0.18 * inch))
story.append(
    Paragraph(
        "Tip: If your actual project path, MySQL path, or Tomcat path is different, replace the sample paths in the commands before running them.",
        styles["GuideSmall"],
    )
)

doc = SimpleDocTemplate(
    str(OUTPUT_PDF),
    pagesize=LETTER,
    leftMargin=0.85 * inch,
    rightMargin=0.85 * inch,
    topMargin=0.85 * inch,
    bottomMargin=0.7 * inch,
)
doc.build(story, onFirstPage=add_page_chrome, onLaterPages=add_page_chrome)
print(str(OUTPUT_PDF.resolve()))
