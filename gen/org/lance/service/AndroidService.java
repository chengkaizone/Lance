/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/lance/workspace/Lance/src/org/lance/service/AndroidService.aidl
 */
package org.lance.service;
public interface AndroidService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements org.lance.service.AndroidService
{
private static final java.lang.String DESCRIPTOR = "org.lance.service.AndroidService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an org.lance.service.AndroidService interface,
 * generating a proxy if needed.
 */
public static org.lance.service.AndroidService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof org.lance.service.AndroidService))) {
return ((org.lance.service.AndroidService)iin);
}
return new org.lance.service.AndroidService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_getName:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getName();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getSalary:
{
data.enforceInterface(DESCRIPTOR);
double _result = this.getSalary();
reply.writeNoException();
reply.writeDouble(_result);
return true;
}
case TRANSACTION_getPersons:
{
data.enforceInterface(DESCRIPTOR);
java.util.List<Person> _result = this.getPersons();
reply.writeNoException();
reply.writeTypedList(_result);
return true;
}
case TRANSACTION_print:
{
data.enforceInterface(DESCRIPTOR);
Person _arg0;
if ((0!=data.readInt())) {
_arg0 = Person.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
java.lang.String _result = this.print(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements org.lance.service.AndroidService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public java.lang.String getName() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getName, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public double getSalary() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
double _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getSalary, _data, _reply, 0);
_reply.readException();
_result = _reply.readDouble();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.util.List<Person> getPersons() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<Person> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getPersons, _data, _reply, 0);
_reply.readException();
_result = _reply.createTypedArrayList(Person.CREATOR);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String print(Person person) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((person!=null)) {
_data.writeInt(1);
person.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_print, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_getName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getSalary = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_getPersons = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_print = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
}
public java.lang.String getName() throws android.os.RemoteException;
public double getSalary() throws android.os.RemoteException;
public java.util.List<Person> getPersons() throws android.os.RemoteException;
public java.lang.String print(Person person) throws android.os.RemoteException;
}
