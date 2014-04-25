#!/usr/bin/env python
# -*- coding:utf-8 -*-

import MySQLdb as db
import datetime

if __name__ == "__main__":
	try:
		conn=db.connect(host='localhost',user='root',passwd='',port=3306)
		cur=conn.cursor()
		conn.select_db('tmailcontest')
		count = cur.execute('select * from tmail_firstseason where type = 1')
		data = cur.fetchall()

#		brand = {}
		user = {}
		for (id,uid,bid,type,date) in data:
			mon = date.month
#			brand.setdefault(bid,{})
#			brand[bid].setdefault(mon, 0)
#			brand[bid][mon] = brand[bid][mon] + 1

			user.setdefault(uid,{})
			user[uid].setdefault(mon,[])
			if not (str(bid) in user[uid][mon]): 	
				user[uid][mon].append(str(bid))

		output = open('user','w')
		for u in user.keys():
			br = {}
			for b in user[u].values():
				for bt in b:
					br.setdefault(bt, 0)
					br[bt] = br[bt] + 1
					if br[bt] > 1:
						user[u].setdefault('t',[])
						user[u]['t'].append(bt)

		for u in user.keys():
			temp = []
			temp.append(str(u))
			temp.append(','.join(user[u].get(4,[])))
			temp.append(','.join(user[u].get(5,[])))
			temp.append(','.join(user[u].get(6,[])))
			temp.append(','.join(user[u].get(7,[])))
			temp.append(','.join(user[u].get(8,[])))
			temp.append(','.join(user[u].get('t',[])))
			s = '|'.join(temp) + '\n'
			output.write(s)

#		output = open('brand','w')
#		for b in brand.keys():
#			temp = []
#			temp.append(str(b))
#			temp.append(str(brand[b].get(4,0)))
#			temp.append(str(brand[b].get(5,0)))
#			temp.append(str(brand[b].get(6,0)))
#			temp.append(str(brand[b].get(7,0)))
#			temp.append(str(brand[b].get(8,0)))
#			s = ','.join(temp) + '\n'
#			output.write(s)
			
		output.close()

		cur.close()
		conn.close()
	except Exception as e:
		print e


