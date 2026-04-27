import { beforeEach, describe, expect, it } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';
import sinon from 'sinon';

import { DATE_FORMAT, DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { Consulta } from '@/shared/model/consulta.model';

import ConsultaService from './consulta.service';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('Consulta Service', () => {
    let service: ConsultaService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new ConsultaService();
      currentDate = new Date();
      elemDefault = new Consulta(
        123,
        'AAAAAAA',
        currentDate,
        currentDate,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        false,
        currentDate,
        currentDate,
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          fechaHoraInicio: dayjs(currentDate).format(DATE_TIME_FORMAT),
          fechaHoraFin: dayjs(currentDate).format(DATE_TIME_FORMAT),
          fechaAlta: dayjs(currentDate).format(DATE_FORMAT),
          fechaBaja: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a Consulta', async () => {
        const returnedFromService = {
          id: 123,
          fechaHoraInicio: dayjs(currentDate).format(DATE_TIME_FORMAT),
          fechaHoraFin: dayjs(currentDate).format(DATE_TIME_FORMAT),
          fechaAlta: dayjs(currentDate).format(DATE_FORMAT),
          fechaBaja: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        const expected = {
          fechaHoraInicio: currentDate,
          fechaHoraFin: currentDate,
          fechaAlta: currentDate,
          fechaBaja: currentDate,
          ...returnedFromService,
        };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Consulta', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Consulta', async () => {
        const returnedFromService = {
          codigo: 'BBBBBB',
          fechaHoraInicio: dayjs(currentDate).format(DATE_TIME_FORMAT),
          fechaHoraFin: dayjs(currentDate).format(DATE_TIME_FORMAT),
          sintomas: 'BBBBBB',
          motivoConsulta: 'BBBBBB',
          examenFisico: 'BBBBBB',
          observaciones: 'BBBBBB',
          indicaciones: 'BBBBBB',
          activa: true,
          fechaAlta: dayjs(currentDate).format(DATE_FORMAT),
          fechaBaja: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };

        const expected = {
          fechaHoraInicio: currentDate,
          fechaHoraFin: currentDate,
          fechaAlta: currentDate,
          fechaBaja: currentDate,
          ...returnedFromService,
        };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Consulta', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Consulta', async () => {
        const patchObject = {
          motivoConsulta: 'BBBBBB',
          observaciones: 'BBBBBB',
          indicaciones: 'BBBBBB',
          activa: true,
          fechaBaja: dayjs(currentDate).format(DATE_FORMAT),
          ...new Consulta(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = {
          fechaHoraInicio: currentDate,
          fechaHoraFin: currentDate,
          fechaAlta: currentDate,
          fechaBaja: currentDate,
          ...returnedFromService,
        };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Consulta', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Consulta', async () => {
        const returnedFromService = {
          codigo: 'BBBBBB',
          fechaHoraInicio: dayjs(currentDate).format(DATE_TIME_FORMAT),
          fechaHoraFin: dayjs(currentDate).format(DATE_TIME_FORMAT),
          sintomas: 'BBBBBB',
          motivoConsulta: 'BBBBBB',
          examenFisico: 'BBBBBB',
          observaciones: 'BBBBBB',
          indicaciones: 'BBBBBB',
          activa: true,
          fechaAlta: dayjs(currentDate).format(DATE_FORMAT),
          fechaBaja: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        const expected = {
          fechaHoraInicio: currentDate,
          fechaHoraFin: currentDate,
          fechaAlta: currentDate,
          fechaBaja: currentDate,
          ...returnedFromService,
        };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Consulta', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Consulta', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Consulta', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
