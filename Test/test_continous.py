import requests
import csv
import json
import time
URL = "http://localhost:8080/api/report_data"
c = 1
with open("top_temp.csv", mode='r') as csv_file:
    csv_reader = csv.reader(csv_file, delimiter=';')
    next(csv_reader)
    # for i in range(22):
    #     next(csv_reader)
    for row in csv_reader:
        print(row[3])
        to_send = {"id": c, "message": row[0], "mqversion": row[1], "mqedition": int(row[2]),
                   "mquilang": row[3], "serialhash": row[4], "os": row[5], "osversion": row[6], "osarch": row[7],
                   "visiblememory": row[8], "freememory": row[9], "cpuname": row[10], "netfxversions": row[11],
                   "originalhash": row[12], "timestamp": row[13],
                   "exceptionobjecttype": row[15], "stacktrace": row[16]}
        json_data = json.dumps(to_send)
        sending = requests.post(url=URL, headers={"Content-Type": "application/json"}, data=json_data)

        print(sending.status_code, sending.reason)
        c += 1
        if c % 50==0:
            response=requests.get(url=URL)
            data=response.json()
            print(data)
            time.sleep(5)
        if c > 500:
            break
csv_file.close()