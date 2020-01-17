#define sm A0
void setup() {
  // put your setup code here, to run once:
  pinMode(sm,INPUT);
  Serial.begin(9600);
}

void loop() {
  // put your main code here, to run repeatedly:
int v= analogRead(sm);
Serial.println(v);
}
