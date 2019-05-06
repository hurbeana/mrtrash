#!python
import requests
from bs4 import BeautifulSoup
import json

url = "https://www.wien.gv.at/umwelt/ma48/beratung/muelltrennung/mistabc.html"
towrite = []
resp = requests.get(url)
soup = BeautifulSoup(resp.content, features="html.parser")
relevantField = soup.find("div", attrs={'class':'editableDocument'})
letters = relevantField.findAll("h2")

for l in letters:
    items = l.next_sibling.next_sibling.findAll("li")
    entry = {}
    for i in items:
        try:
            k,v = i.text.split(": ")
        except:
            for i in i.findAll("li"):
                places = v.split(", ")
        places = v.split(", ")
        entry['type'] = k
        entry['wastePlaces'] = places
        towrite.append(entry.copy())
        
print(json.dumps(towrite, indent=4, sort_keys=True))

with open("muellABC.json", 'w') as out:
    json.dump(towrite, out)