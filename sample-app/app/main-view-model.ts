import { Observable } from '@nativescript/core'
const coreApplication = require("@nativescript/core/application");
declare const co: any

export class HelloWorldModel extends Observable {
  private _message: string

  private dataDomeSDKWrapper: any

  constructor() {
    super()
    this.updateMessage()

    let androidApp = coreApplication.android.context
    this.dataDomeSDKWrapper = new co.datadome.ns.wrapper.DataDomeSDKWrapper(androidApp, "key", "1.0.0", true)
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
    
  }

  private updateMessage() {
    this.message = ""
  }
}
