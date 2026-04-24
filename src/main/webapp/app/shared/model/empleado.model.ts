import { type ICargo } from '@/shared/model/cargo.model';
import { type IEstadoLaboral } from '@/shared/model/estado-laboral.model';
import { type IPersona } from '@/shared/model/persona.model';
import { type ITipoEmpleado } from '@/shared/model/tipo-empleado.model';

export interface IEmpleado {
  id?: number;
  legajo?: string;
  fechaIngreso?: Date;
  fechaBaja?: Date | null;
  activo?: boolean;
  persona?: IPersona;
  tipoEmpleado?: ITipoEmpleado;
  estadoLaboral?: IEstadoLaboral;
  cargo?: ICargo;
}

export class Empleado implements IEmpleado {
  constructor(
    public id?: number,
    public legajo?: string,
    public fechaIngreso?: Date,
    public fechaBaja?: Date | null,
    public activo?: boolean,
    public persona?: IPersona,
    public tipoEmpleado?: ITipoEmpleado,
    public estadoLaboral?: IEstadoLaboral,
    public cargo?: ICargo,
  ) {
    this.activo = this.activo ?? false;
  }
}
