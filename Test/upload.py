import csv
import requests
import json

URL = "http://localhost:8080/api/report_data"
c = 1
for i in range(1,11):
    with open("top_temp.csv", mode='r') as csv_file:
        csv_reader = csv.reader(csv_file, delimiter=';')
        next(csv_reader)
        # for i in range(22):
        #     next(csv_reader)
        for row in csv_reader:
            print(row[3])
            to_send = {"id": c, "message": row[0],"mqversion":row[1], "mqedition": int(row[2]),
                      "mquilang": row[3], "serialhash": row[4], "os": row[5], "osversion": row[6], "osarch": row[7],
                       "visiblememory": row[8], "freememory": row[9], "cpuname": row[10],"netfxversions": row[11],
                      "originalhash": row[12], "timestamp": row[13],
                      "exceptionobjecttype": row[15], "stacktrace": row[16]}
            json_data = json.dumps(to_send)
            sending = requests.post(url=URL, headers={"Content-Type": "application/json"}, data=json_data)

            print(sending.status_code, sending.reason)
            c += 1
    csv_file.close()
# with open("top_temp.csv", mode='r') as csv_file:
#     csv_reader = csv.reader(csv_file, delimiter=';')
#     next(csv_reader)
#     # for i in range(22):
#     #     next(csv_reader)
#     for row in csv_reader:
#         print(row[3])
#         to_send = {"id": c, "message": row[0],"mqversion":row[1], "mqedition": int(row[2]),
#                   "mquilang": row[3], "serialhash": row[4], "os": row[5], "osversion": row[6], "osarch": row[7],
#                    "visiblememory": row[8], "freememory": row[9], "cpuname": row[10],"netfxversions": row[11],
#                   "originalhash": row[12], "timestamp": row[13],
#                   "exceptionobjecttype": row[15], "stacktrace": row[16]}
#         json_data = json.dumps(to_send)
#         sending = requests.post(url=URL, headers={"Content-Type": "application/json"}, data=json_data)
#
#         print(sending.status_code, sending.reason)
#         c += 1
