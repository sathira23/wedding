from pathlib import Path

from docx import Document
from docx.enum.section import WD_SECTION
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.oxml import OxmlElement
from docx.oxml.ns import qn
from docx.shared import Inches, Pt, RGBColor


OUTPUT_DOCX = Path("Wedding-Package-Management-New-PC-Setup-Guide.docx")


def set_cell_text(cell, label, value):
    cell.text = ""
    paragraph = cell.paragraphs[0]
    run = paragraph.add_run(label)
    run.bold = True
    run.font.name = "Arial"
    run._element.rPr.rFonts.set(qn("w:eastAsia"), "Arial")
    run.font.size = Pt(11)
    run = paragraph.add_run(value)
    run.font.name = "Arial"
    run._element.rPr.rFonts.set(qn("w:eastAsia"), "Arial")
    run.font.size = Pt(11)


def shade_cell(cell, fill):
    tc_pr = cell._tc.get_or_add_tcPr()
    shd = OxmlElement("w:shd")
    shd.set(qn("w:fill"), fill)
    tc_pr.append(shd)


def set_table_borders(table):
    tbl = table._tbl
    tbl_pr = tbl.tblPr
    borders = OxmlElement("w:tblBorders")
    for edge in ("top", "left", "bottom", "right", "insideH", "insideV"):
        elem = OxmlElement(f"w:{edge}")
        elem.set(qn("w:val"), "single")
        elem.set(qn("w:sz"), "6")
        elem.set(qn("w:space"), "0")
        elem.set(qn("w:color"), "D9D9D9")
        borders.append(elem)
    tbl_pr.append(borders)


def set_cell_margins(table, top=90, start=120, bottom=90, end=120):
    tbl = table._tbl
    tbl_pr = tbl.tblPr
    tbl_cell_mar = OxmlElement("w:tblCellMar")
    for name, value in (("top", top), ("start", start), ("bottom", bottom), ("end", end)):
        node = OxmlElement(f"w:{name}")
        node.set(qn("w:w"), str(value))
        node.set(qn("w:type"), "dxa")
        tbl_cell_mar.append(node)
    tbl_pr.append(tbl_cell_mar)


def add_bullet(document, text):
    paragraph = document.add_paragraph(style="List Bullet")
    paragraph.paragraph_format.space_after = Pt(6)
    run = paragraph.add_run(text)
    run.font.name = "Arial"
    run._element.rPr.rFonts.set(qn("w:eastAsia"), "Arial")
    run.font.size = Pt(11)


def add_number(document, text):
    paragraph = document.add_paragraph(style="List Number")
    paragraph.paragraph_format.space_after = Pt(6)
    run = paragraph.add_run(text)
    run.font.name = "Arial"
    run._element.rPr.rFonts.set(qn("w:eastAsia"), "Arial")
    run.font.size = Pt(11)


def add_body(document, text):
    paragraph = document.add_paragraph()
    paragraph.paragraph_format.space_after = Pt(8)
    run = paragraph.add_run(text)
    run.font.name = "Arial"
    run._element.rPr.rFonts.set(qn("w:eastAsia"), "Arial")
    run.font.size = Pt(11)


doc = Document()
section = doc.sections[0]
section.page_width = Inches(8.5)
section.page_height = Inches(11)
section.top_margin = Inches(0.9)
section.bottom_margin = Inches(0.8)
section.left_margin = Inches(0.85)
section.right_margin = Inches(0.85)

styles = doc.styles
styles["Normal"].font.name = "Arial"
styles["Normal"]._element.rPr.rFonts.set(qn("w:eastAsia"), "Arial")
styles["Normal"].font.size = Pt(11)

for style_name, size in (("Title", 22), ("Subtitle", 11), ("Heading 1", 15), ("Heading 2", 12)):
    style = styles[style_name]
    style.font.name = "Arial"
    style._element.rPr.rFonts.set(qn("w:eastAsia"), "Arial")
    style.font.size = Pt(size)

styles["Title"].font.bold = True
styles["Title"].font.color.rgb = RGBColor(27, 63, 107)
styles["Subtitle"].font.color.rgb = RGBColor(110, 110, 110)
styles["Heading 1"].font.bold = True
styles["Heading 1"].font.color.rgb = RGBColor(27, 63, 107)
styles["Heading 2"].font.bold = True
styles["Heading 2"].font.color.rgb = RGBColor(52, 52, 52)

header_para = section.header.paragraphs[0]
header_para.alignment = WD_ALIGN_PARAGRAPH.LEFT
header_run = header_para.add_run("Wedding Package Management | New PC Setup Guide")
header_run.font.name = "Arial"
header_run._element.rPr.rFonts.set(qn("w:eastAsia"), "Arial")
header_run.font.size = Pt(9)
header_run.font.color.rgb = RGBColor(100, 100, 100)

footer_para = section.footer.paragraphs[0]
footer_para.alignment = WD_ALIGN_PARAGRAPH.CENTER
footer_run = footer_para.add_run("Prepared for local setup and deployment")
footer_run.font.name = "Arial"
footer_run._element.rPr.rFonts.set(qn("w:eastAsia"), "Arial")
footer_run.font.size = Pt(9)
footer_run.font.color.rgb = RGBColor(120, 120, 120)

title = doc.add_paragraph(style="Title")
title.alignment = WD_ALIGN_PARAGRAPH.LEFT
title.add_run("Wedding Package Management\nNew PC Setup Guide")

subtitle = doc.add_paragraph(style="Subtitle")
subtitle.add_run("Use this guide to install prerequisites, configure MySQL, build the project, deploy it to Tomcat, and log in successfully.")

table = doc.add_table(rows=4, cols=2)
table.style = "Table Grid"
set_table_borders(table)
set_cell_margins(table)
table.columns[0].width = Inches(2.1)
table.columns[1].width = Inches(4.9)
rows = [
    ("Project type: ", "Java web application using JSP, Servlets, JDBC, Maven, and Tomcat 9"),
    ("Database: ", "MySQL schema: wedding_package_management"),
    ("WAR file: ", "target\\wedding-package-management.war"),
    ("Default logins: ", "admin/admin, organizer1/organizer1, organizer2/organizer2"),
]
for idx, (label, value) in enumerate(rows):
    shade_cell(table.rows[idx].cells[0], "EEF3F8")
    set_cell_text(table.rows[idx].cells[0], label, "")
    set_cell_text(table.rows[idx].cells[1], "", value)

doc.add_paragraph()

doc.add_paragraph("1. Software You Need", style="Heading 1")
add_bullet(doc, "Java JDK 11")
add_bullet(doc, "Apache Maven")
add_bullet(doc, "MySQL Server 8.x")
add_bullet(doc, "Apache Tomcat 9.x")
add_bullet(doc, "Optional: MySQL Workbench for easier database management")

add_body(
    doc,
    "Recommended official downloads: Maven - https://maven.apache.org/install, Tomcat 9 - https://tomcat.apache.org/download-90.html, MySQL Community Server - https://dev.mysql.com/downloads/mysql/.",
)

doc.add_paragraph("2. Copy the Project", style="Heading 1")
add_body(
    doc,
    "Copy the full project folder to the new PC. Example location: D:\\Projects\\Wedding Package Management. You can use another folder, but update the commands below to match your actual path.",
)

doc.add_paragraph("3. Verify Java and Maven", style="Heading 1")
add_body(doc, "Open PowerShell and run these commands:")
for cmd in ["java -version", "mvn -version"]:
    p = doc.add_paragraph()
    p.paragraph_format.left_indent = Inches(0.3)
    run = p.add_run(cmd)
    run.font.name = "Consolas"
    run._element.rPr.rFonts.set(qn("w:eastAsia"), "Consolas")
    run.font.size = Pt(10.5)
    run.bold = True

doc.add_paragraph("4. Start MySQL Server", style="Heading 1")
add_body(
    doc,
    "If MySQL was installed as a Windows service, verify it is running. The common service name is MySQL80, but your PC may show a different name in Services.",
)
for cmd in ["sc query MySQL80", "net start MySQL80"]:
    p = doc.add_paragraph()
    p.paragraph_format.left_indent = Inches(0.3)
    run = p.add_run(cmd)
    run.font.name = "Consolas"
    run._element.rPr.rFonts.set(qn("w:eastAsia"), "Consolas")
    run.font.size = Pt(10.5)

doc.add_paragraph("5. Create the Database", style="Heading 1")
add_body(
    doc,
    "Run the schema file included in the project. It creates the wedding_package_management database and inserts sample event and venue data.",
)
for cmd in [
    'mysql -u root -p < "D:\\Projects\\Wedding Package Management\\db-schema.sql"',
    '& "C:\\Program Files\\MySQL\\MySQL Server 8.4\\bin\\mysql.exe" -u root -p < "D:\\Projects\\Wedding Package Management\\db-schema.sql"',
]:
    p = doc.add_paragraph()
    p.paragraph_format.left_indent = Inches(0.3)
    run = p.add_run(cmd)
    run.font.name = "Consolas"
    run._element.rPr.rFonts.set(qn("w:eastAsia"), "Consolas")
    run.font.size = Pt(10.5)

doc.add_paragraph("6. Update Database Password if Needed", style="Heading 1")
add_body(
    doc,
    "The application reads database credentials from src\\main\\webapp\\WEB-INF\\web.xml. The current defaults are root / password. If your MySQL password is different, edit jdbc.username or jdbc.password before building.",
)

doc.add_paragraph("7. Build the Project", style="Heading 1")
add_body(doc, "Go to the project folder and build the WAR file with Maven:")
for cmd in [
    'cd "D:\\Projects\\Wedding Package Management"',
    "mvn clean package",
]:
    p = doc.add_paragraph()
    p.paragraph_format.left_indent = Inches(0.3)
    run = p.add_run(cmd)
    run.font.name = "Consolas"
    run._element.rPr.rFonts.set(qn("w:eastAsia"), "Consolas")
    run.font.size = Pt(10.5)

doc.add_paragraph("8. Set Tomcat Variables and Deploy", style="Heading 1")
add_body(
    doc,
    "Update the sample paths below to your real JDK 11 and Tomcat 9 installation folders, then copy the built WAR into Tomcat webapps.",
)
for cmd in [
    '$env:JAVA_HOME="C:\\Program Files\\Microsoft\\jdk-11.0.x"',
    '$env:CATALINA_HOME="C:\\apache-tomcat-9.0.xxx"',
    'Copy-Item "D:\\Projects\\Wedding Package Management\\target\\wedding-package-management.war" "$env:CATALINA_HOME\\webapps\\"',
]:
    p = doc.add_paragraph()
    p.paragraph_format.left_indent = Inches(0.3)
    run = p.add_run(cmd)
    run.font.name = "Consolas"
    run._element.rPr.rFonts.set(qn("w:eastAsia"), "Consolas")
    run.font.size = Pt(10.5)

doc.add_paragraph("9. Start Tomcat and Open the App", style="Heading 1")
for cmd in [
    'cd "$env:CATALINA_HOME\\bin"',
    ".\\startup.bat",
]:
    p = doc.add_paragraph()
    p.paragraph_format.left_indent = Inches(0.3)
    run = p.add_run(cmd)
    run.font.name = "Consolas"
    run._element.rPr.rFonts.set(qn("w:eastAsia"), "Consolas")
    run.font.size = Pt(10.5)
add_body(doc, "Wait 10 to 20 seconds, then open: http://localhost:8080/wedding-package-management/")

doc.add_paragraph("10. Login Credentials", style="Heading 1")
add_bullet(doc, "Admin: admin / admin")
add_bullet(doc, "Organizer: organizer1 / organizer1")
add_bullet(doc, "Organizer: organizer2 / organizer2")

doc.add_paragraph("11. Quick Troubleshooting", style="Heading 1")
add_number(doc, "If the browser shows a database error, confirm MySQL is running and the password in web.xml matches your real MySQL password.")
add_number(doc, "If the browser shows 404, confirm wedding-package-management.war was copied into Tomcat webapps and Tomcat started successfully.")
add_number(doc, "If Tomcat fails to start, make sure you are using Tomcat 9, not Tomcat 10 or 11.")
add_number(doc, "If mvn is not recognized, add Maven bin to PATH and open a new PowerShell window.")
add_number(doc, "If java is not recognized, fix JAVA_HOME and PATH, then reopen PowerShell.")

doc.add_paragraph("12. Final Run Order", style="Heading 1")
for item in [
    "Install Java 11, Maven, MySQL Server, and Tomcat 9.",
    "Copy the project to the new PC.",
    "Start MySQL Server.",
    "Run db-schema.sql.",
    "Update web.xml if your DB password is different.",
    "Run mvn clean package.",
    "Copy the WAR file to Tomcat webapps.",
    "Start Tomcat.",
    "Open the application and log in.",
]:
    add_number(doc, item)

doc.save(OUTPUT_DOCX)
print(str(OUTPUT_DOCX.resolve()))
