
try:
    with open(r'core\resources\src\main\res\values\strings.xml', 'r', encoding='utf-8') as f:
        for i, line in enumerate(f):
            if 'name="convert"' in line:
                print(f"Line {i+1}: {line.strip()}")
except Exception as e:
    print(e)
