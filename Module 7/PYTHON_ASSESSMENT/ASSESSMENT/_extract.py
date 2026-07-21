import pdfplumber

text = ""
with pdfplumber.open("ASSESSMENT/Python_Coding_CaseStudy.pdf") as pdf:
    for page in pdf.pages:
        text += (page.extract_text() or "") + "\n\n"

with open("ASSESSMENT/_case.txt", "w", encoding="utf-8") as f:
    f.write(text)

print(text)
