import re


def clean_text(text):
    # 1. Zamiana na małe litery
    text = text.lower()

    # 2. Usunięcie znaków spoza ASCII
    text = text.encode("ascii", errors="ignore").decode()

    # 3. Usunięcie interpunkcji (pozostawiając tylko litery, cyfry i spacje)
    text = re.sub(r"[^a-z\s]", "", text)

    # 4. Redukcja wielokrotnych spacji do jednej
    text = re.sub(r"\s+", " ", text)

    return text.strip()


with open("testES1.txt", "r", encoding="utf-8") as f:
    raw_text = f.read()

cleaned = clean_text(raw_text)

with open("testES.txt", "w", encoding="utf-8") as f:
    f.write(cleaned)

print("Zapisano czysty tekst do pliku")
