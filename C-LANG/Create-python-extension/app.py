from flask import Flask, jsonify, request
import mymath  # our C extension

app = Flask(__name__)

@app.route('/factorial', methods=['GET'])
def factorial_api():
    try:
        n = int(request.args.get('n', 1))
        result = mymath.factorial(n)
        return jsonify({'number': n, 'factorial': result})
    except Exception as e:
        return jsonify({'error': str(e)}), 400

if __name__ == '__main__':
    app.run(debug=True)