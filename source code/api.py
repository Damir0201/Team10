#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Wed Oct 14 18:10:29 2020

@author: team10
"""

from flask import Flask, jsonify, abort, request,render_template,send_from_directory
import mysql.connector
import hashlib
import numpy as np

app = Flask(__name__)


def minkowskiDist(v1, v2, p):
    
    dist = 0.0
    for i in range(len(v1)):
        dist += abs(v1[i] - v2[i])**p
    return dist**(1/p)

class Item(object):
    def __init__(self, name, features):
        
        self.name = name
        
        self.features = np.array(features)
        
    def getName(self):
        return self.name
    
    
    
    def setColor(self, color):
        self.color = color
    
    def dimensionality(self):
        return len(self.features)
    
    def getFeatures(self):
        return self.features
    
    def distance(self, other):
        
        return minkowskiDist(self.getFeatures(),
                             other.getFeatures(), 2)
                             
    def __str__(self):
        return self.name

def gethash(s):
    hashed=hashlib.sha256(s.encode()).hexdigest()
    return hashed



def register(email,password):
    db_connection = mysql.connector.connect(host="localhost",user="team10",passwd="ZaQ11wsx!", database="db")
    db_cursor = db_connection.cursor()
    query = f"SELECT* FROM users WHERE email = '{email.strip().lower()}'"
    db_cursor.execute(query)
    result = db_cursor.fetchall()
    if len(result) == 0:
        query = f"INSERT INTO users(email,password) VALUES('{email.strip().lower()}', '{str(gethash(password))}')"
        db_cursor.execute(query)
        db_connection.commit()
                
        return True

    else:
        return False

def new_note(email,theme,text):
    db_connection = mysql.connector.connect(host="localhost",user="team10",passwd="ZaQ11wsx!", database="db")
    db_cursor = db_connection.cursor()
    query = f"INSERT INTO notes(user,theme,note_text) VALUES('{email.strip().lower()}', '{theme}', '{text}')"
    db_cursor.execute(query)
    db_connection.commit()
                
    


def auth(email, password):
    db_connection = mysql.connector.connect(host="localhost",user="team10",passwd="ZaQ11wsx!", database="db")
    db_cursor = db_connection.cursor()
    query = f"SELECT* FROM users WHERE email = '{email.strip()}' AND password='{str(gethash(password))}'"
    db_cursor.execute(query)
    result = db_cursor.fetchall()
    if len(result) > 0:
        return True
    else:
        return False
    
def themes(email):
    db_connection = mysql.connector.connect(host="localhost",user="team10",passwd="ZaQ11wsx!", database="db")
    db_cursor = db_connection.cursor()
    query = f"SELECT theme FROM notes WHERE user = '{email.strip()}'"
    db_cursor.execute(query)
    result = db_cursor.fetchall()
    response = ""
    for i,row in enumerate(result):
        if i != 0:
            response = response+";"
        response+=str(row[0])
    return response

def text_by_theme(email,theme):
    db_connection = mysql.connector.connect(host="localhost",user="team10",passwd="ZaQ11wsx!", database="db")
    db_cursor = db_connection.cursor()
    query = f"SELECT note_text FROM notes WHERE user = '{email.strip()}' AND theme = '{theme}'"
    db_cursor.execute(query)
    result = db_cursor.fetchall()
    response = ""
    for row in result:
        response = str(row[0])
        
    return response

@app.route('/api/v1.0/authorization', methods = ['GET', 'POST'])
def authorization():

    if not request.json or not 'user' in request.json :
        abort(400)
    if not 'password' in request.json:
        abort(400)
    
   
    try:
        
        user=request.json['user'].lower()
        pas = request.json['password'].lower()
        if auth(user, pas):
            return 'success', 201
        else:
            return 'fail', 201
            
      
    except Exception as error:
        
        mess=str(error)
        return jsonify({'error': mess}), 201
    

@app.route('/api/v1.0/add_note', methods = ['GET', 'POST'])
def add_note():

    if not request.json or not 'user' in request.json :
        abort(400)
    
    if not 'theme' in request.json or not 'text' in request.json:
        abort(400)
    
   
    try:
        
        user=request.json['user'].lower()
        text=request.json['text']
        theme=request.json['theme']
        new_note(user,theme,text)
        return 'success', 201
    
            
      
    except Exception as error:
        
        mess=str(error)
        return jsonify({'error': mess}), 201

    
@app.route('/api/v1.0/get_themes', methods = ['GET', 'POST'])
def get_themes():

    if not request.json or not 'user' in request.json :
        abort(400)
    
    
   
    try:
        
        user=request.json['user'].lower()
   
        return themes(user), 201
    
            
      
    except Exception as error:
        
        mess=str(error)
        return jsonify({'error': mess}), 201
    
@app.route('/api/v1.0/get_text', methods = ['GET', 'POST'])
def get_text():

    if not request.json or not 'user' in request.json :
        abort(400)
    
    if not 'theme' in request.json:
        abort(400)
   
    try:
        
        user=request.json['user'].lower()
        theme=request.json['theme']
        
        return text_by_theme(user,theme), 201
    
            
      
    except Exception as error:
        
        mess=str(error)
        return jsonify({'error': mess}), 201

@app.route('/api/v1.0/registration', methods = ['GET', 'POST'])
def registration():

    if not request.json or not 'user' in request.json :
        abort(400)
    if not 'password' in request.json:
        abort(400)
    
   
    try:
        user=request.json['user'].lower()
        if len(user)>4:
            
            pas = request.json['password'].lower()
            if register(user, pas):
                return 'success', 201
            else:
                return 'fail', 201
        else:
           return 'fail', 201 
            
      
    except Exception as error:
        
        mess=str(error)
        return jsonify({'error': mess}), 201



def new_answers(email,is_concern,emo_state,reason,future):
    db_connection = mysql.connector.connect(host="localhost",user="team10",passwd="ZaQ11wsx!", database="db")
    db_cursor = db_connection.cursor()
    
    query = f"SELECT* FROM answers WHERE user = '{email.strip().lower()}'"
    db_cursor.execute(query)
    result = db_cursor.fetchall()
    if len(result) == 0:
        values = f"'{email.strip().lower()}', {is_concern}, {emo_state}, '{reason}', {future}"
        query = f"INSERT INTO answers(user,is_concern,emo_state,reason,future) VALUES({values})"
        db_cursor.execute(query)
        db_connection.commit()


    else:
        query = f"UPDATE answers SET is_concern={is_concern}, emo_state ={emo_state}, reason='{reason}', future={future} WHERE user = '{email.strip().lower()}'"
        db_cursor.execute(query)
        db_connection.commit()
    
    

@app.route('/api/v1.0/questions', methods = ['GET', 'POST'])
def questions():

    if not request.json or not 'is_concern' in request.json :
        abort(400)
        
    if not 'emo_state' in request.json or not 'reason' in request.json :
        abort(400)
    
    if not 'future' in request.json or not 'user' in request.json:
        abort(400)
  
    try:
        user=request.json['user'].lower()
        is_concern = float(request.json['is_concern'])
        emo_state = float(request.json['emo_state'])
        reason = (request.json['reason'])
        future = float(request.json['future'])
        new_answers(user,is_concern,emo_state,reason,future)
        return 'success', 201
        
        
            
      
    except Exception as error:
        
        mess=str(error)
        return jsonify({'error': mess}), 201
    


def list_answers(email):
    db_connection = mysql.connector.connect(host="localhost",user="team10",passwd="ZaQ11wsx!", database="db")
    db_cursor = db_connection.cursor()
    
    query = f"SELECT is_concern,emo_state, future FROM answers WHERE user = '{email.strip().lower()}'"
    db_cursor.execute(query)
    result = db_cursor.fetchall()
    item_features = []
    for row in result:
        f1 = float(row[0]) 
        f2 = float(row[1]) 
        f3 = float(row[2])
        item_features.append(f1)
        item_features.append(f2)
        item_features.append(f3)
    item_name = email
    main_item = Item(item_name, item_features)
    
    query = f"SELECT is_concern,emo_state, future,user FROM answers WHERE user != '{email.strip().lower()}'"
    db_cursor.execute(query)
    result = db_cursor.fetchall()
    distances = []
    
    for row in result:
        try:
            features = []
            f1 = float(row[0]) 
            f2 = float(row[1]) 
            f3 = float(row[2])
            name = (row[3])
            features.append(f1)
            features.append(f2)
            features.append(f3)
            another_item= Item(name, features)
            dif = main_item.distance(another_item)
            distances.append((dif , another_item))
        except:
            continue
        
    distances.sort(key=lambda x: x[0])
    response = ""
    for i,distance in enumerate(distances):
        if i != 0:
            response+=";"
        response += str(distance[1])
    return response
        
    
   
@app.route('/api/v1.0/analysis', methods = ['GET', 'POST'])
def analysis():

    if not request.json or not 'user' in request.json :
        abort(400)
        
    
  
    try:
        user=request.json['user'].lower()
        return list_answers(user),201

    except Exception as error:
        
        mess=str(error)
        return jsonify({'error': mess}), 201


@app.route('/')
def index():
    return "hello from team10!"



if __name__ == '__main__':
    print('started')
    app.run(host= '0.0.0.0',port='9000', threaded=True)