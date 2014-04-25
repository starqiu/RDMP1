def parse_date(raw_date):
    entry_date = raw_date.decode("gbk")
    month = int(entry_date[0])
    if len(entry_date) == 5:
        day = 10 * int(entry_date[2]) + int(entry_date[3])
    else:
        day = int(entry_date[2])
    return 2013, month, day

def split_file(raw_file, seperate_day, begin_date):
    train = open("train.csv", "w")
    validation = open("validation.csv", "w")
    raw_file.readline()
    for line in raw_file.readlines():
        line = line.strip()
        entry = line.split(",")
        entry_date = date(*parse_date(entry[3]))
        date_delta = (entry_date - begin_date).days
        if date_delta < seperate_day:
            train.write(",".join(entry[:3]) + "," + str(date_delta) + "\n")
        elif int(entry[2]) == 1:
            validation.write(",".join(entry[:2]) + "\n")
            print ",".join(entry[:2])
    validation.write("99999999999,9" + "\n")
    train.close()
    validation.close()

def generate_result(validation):
    entrys = validation.readlines()
    entrys.sort(key=lambda x: x.split(",")[0])
    result = open("result.txt", "w")
    for index, entry in enumerate(entrys):
        uid, tid = entry.strip().split(",")
        if index == 0:
            cur_id = uid
            cur_result = [tid]
        elif uid == cur_id:
            cur_result.append(tid)
        else:
            result.write(cur_id + "\t" + ",".join(set(cur_result)) + "\n")
            cur_id = uid
            cur_result = [tid]
    result.close()

from collections import defaultdict

predict_num = 0
hit_num = 0
brand = 0
result = defaultdict(set)
f = open("result")
for line in f.readlines():
    line = line.strip()
    uid, bid = line.split("\t")
    result[uid] = bid.split(",")
    brand += len(result[uid])
f.close()


f = open("predict.txt")
for line in f.readlines():
    line = line.strip()
    uid, bid = line.split("\t")
    bid = bid.split(",")
    predict_num += len(bid)
    if uid not in result:
        continue
    else:
        for i in bid:
            if i in result[uid]:
                hit_num += 1

print "predict num is ", predict_num
print "hit num is ", hit_num
print "total brand is ", brand

precision = float(hit_num)/predict_num
callrate = float(hit_num)/brand
print "precision is ", precision
print "call rate is ", callrate

print "F1 is ", 2*precision*callrate/(precision+callrate)