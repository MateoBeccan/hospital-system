import { type IEmpleado } from '@/shared/model/empleado.model';
import { type ITurnoLaboral } from '@/shared/model/turno-laboral.model';

export interface IEnfermero {
  id?: number;
  matricula?: string;
  fechaMatriculacion?: Date | null;
  activo?: boolean;
  fechaAlta?: Date;
  fechaBaja?: Date | null;
  empleado?: IEmpleado;
  turnoLaboral?: ITurnoLaboral | null;
}

export class Enfermero implements IEnfermero {
  constructor(
    public id?: number,
    public matricula?: string,
    public fechaMatriculacion?: Date | null,
    public activo?: boolean,
    public fechaAlta?: Date,
    public fechaBaja?: Date | null,
    public empleado?: IEmpleado,
    public turnoLaboral?: ITurnoLaboral | null,
  ) {
    this.activo = this.activo ?? false;
  }
}
