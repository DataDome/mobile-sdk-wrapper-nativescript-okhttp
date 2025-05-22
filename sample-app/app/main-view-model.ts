import { Observable } from '@nativescript/core'
declare const co: any

export class HelloWorldModel extends Observable {
  private _counter: number
  private _message: string

  constructor() {
    super()

    // Initialize default values.
    this._counter = 42
    this.updateMessage()
  }

  get message(): string {
    return this._message
  }

  set message(value: string) {
    if (this._message !== value) {
      this._message = value
      this.notifyPropertyChange('message', value)
    }
  }

  onTap() {
    // TODO send request
    const dataDomeSDK = new co.datadome.ns.wrapper.DataDomeSDK();
  }

  private updateMessage() {
    this.message = ""
  }
}
