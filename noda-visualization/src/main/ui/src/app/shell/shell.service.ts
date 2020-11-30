import { Routes, Route } from '@angular/router';

import { ShellComponent } from './shell.component';

/**
 * Provides helper methods to create routes.
 */
export class Shell {
  /**
   * Creates routes using the shell component and authentication.
   * @param routes The routes to add.
   * @return The new route using shell as the base.
   */
  static childRoutes(routes: Routes): Route {
    return {
      path: '',
      component: ShellComponent,
      children: routes,
      // Reuse ShellComponent instance when navigating between child views
      data: { reuse: true },
    };
  }
}

export const themeFromMapBox =
  'https://api.mapbox.com/styles/v1/nikoskous/cki30mfyw42ll19om2sgz8e2q/tiles/256/{z}/{x}/{y}@2x?access_token=pk.eyJ1Ijoibmlrb3Nrb3VzIiwiYSI6ImNrMnRjajM5NDBxZXczbXA1YmxueGdhcmMifQ.O0Fz93cltHQ10OfqGBS7FQ';
